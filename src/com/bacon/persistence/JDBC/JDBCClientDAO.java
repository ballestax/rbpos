/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bacon.persistence.JDBC;


import com.bacon.DBManager;
import com.bacon.domain.Client;
import com.bacon.persistence.SQLExtractor;
import com.bacon.persistence.SQLLoader;
import com.bacon.persistence.dao.ClientDAO;
import com.bacon.persistence.dao.DAOException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author LuisR
 */
public class JDBCClientDAO implements ClientDAO {

    public static final String TABLE_NAME = "clients";
    public static final String NAMED_PARAM_WHERE = "{where}";
    public static final String NAMED_PARAM_ORDER_BY = "{orderby}";
    private static final Logger logger = Logger.getLogger(JDBCClientDAO.class.getCanonicalName());
    private final DataSource dataSource;
    private final SQLLoader sqlStatements;
    protected static final String CREATE_CLIENTE_TABLE_KEY = "CREATE_CLIENTES_TABLE";
    protected static final String ADD_CLIENTE_KEY = "ADD_CLIENTE";
    protected static final String UPDATE_CLIENTE_KEY = "UPDATE_CLIENTE";
    protected static final String GET_CLIENTE_KEY = "GET_CLIENTE";
    protected static final String DELETE_CLIENTE_KEY = "DELETE_CLIENTE";

    public JDBCClientDAO(DataSource dataSource, SQLLoader sqlStatements) {
        this.dataSource = dataSource;
        this.sqlStatements = sqlStatements;
    }

    public final void init() throws DAOException, RemoteException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            if (DBManager.tableExists(TABLE_NAME, conn)) {
                return;
            }
            ps = sqlStatements.buildSQLStatement(conn, CREATE_CLIENTE_TABLE_KEY);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Cannot create client table", e);
        } catch (IOException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Cannot create client table", e);
        } finally {
            DBManager.closeStatement(ps);
            DBManager.closeConnection(conn);
        }
    }

    public Client getClientBy(String query) throws DAOException {
        String retrieveImporter;
        try {
            SQLExtractor sqlExtractorWhere = new SQLExtractor(query, SQLExtractor.Type.WHERE);;
            Map<String, String> namedParams = new HashMap<String, String>();
            namedParams.put(NAMED_PARAM_WHERE, sqlExtractorWhere.extractWhere());
            retrieveImporter = sqlStatements.getSQLString(GET_CLIENTE_KEY, namedParams);

        } catch (SQLException e) {
            throw new DAOException("Could not properly retrieve the client", e);
        } catch (IOException e) {
            throw new DAOException("Could not properly retrieve the client", e);
        }
        Connection conn = null;
        PreparedStatement retrieve = null;
        ResultSet rs = null;
        Client client = null;
        try {
            conn = dataSource.getConnection();
            retrieve = conn.prepareStatement(retrieveImporter);
            rs = retrieve.executeQuery();
            while (rs.next()) {
                client = new Client();
                client.setCellphone(rs.getString(1));
                client.setNames(rs.getString(2));
                client.setLastName(rs.getString(3));
            }
        } catch (SQLException e) {
            throw new DAOException("Could not properly retrieve the Client: " + e);
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closeStatement(retrieve);
            DBManager.closeConnection(conn);
        }
        return client;
    }

    @Override
    public Client getClient(long id) throws DAOException {
        return getClientBy("identificacion=" + id);
    }

    @Override
    public ArrayList<Client> getClientList() throws DAOException {
        return getClientListBy("", "");
    }

    public ArrayList<Client> getClientListBy(String where, String order) throws DAOException {
        String retrieveClient;
        ArrayList<Client> lista = new ArrayList<>();
        try {
            SQLExtractor sqlExtractorWhere = new SQLExtractor(where, SQLExtractor.Type.WHERE);;
            SQLExtractor sqlExtractorOrderBy = new SQLExtractor(order, SQLExtractor.Type.ORDER_BY);;
            Map<String, String> namedParams = new HashMap<>();
            namedParams.put(NAMED_PARAM_WHERE, sqlExtractorWhere.extractWhere());
            namedParams.put(NAMED_PARAM_ORDER_BY, sqlExtractorOrderBy.extractOrderBy());
            retrieveClient = sqlStatements.getSQLString(GET_CLIENTE_KEY, namedParams);

        } catch (SQLException e) {
            throw new DAOException("Could not properly retrieve the client", e);
        } catch (IOException e) {
            throw new DAOException("Could not properly retrieve the client", e);
        }
        Connection conn = null;
        PreparedStatement retrieve = null;
        ResultSet rs = null;
        Client client = null;
        try {
            conn = dataSource.getConnection();
            retrieve = conn.prepareStatement(retrieveClient);
            rs = retrieve.executeQuery();
            while (rs.next()) {
                client = new Client();
                client.setCellphone(rs.getString(1));
                client.setNames(rs.getString(2));                
                client.setLastName(rs.getString(3));
                lista.add(client);
            }
        } catch (SQLException e) {
            throw new DAOException("Could not properly retrieve the Client: " + e);
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closeStatement(retrieve);
            DBManager.closeConnection(conn);
        }
        return lista;
    }

    @Override
    public void addClient(Client client) throws DAOException {
        if (client == null) {
            throw new IllegalArgumentException("Null client");
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            Object[] parameters = {
                client.getCellphone(),
                client.getNames(),
                client.getLastName()
            };
            ps = sqlStatements.buildSQLStatement(conn, ADD_CLIENTE_KEY, parameters);

            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Cannot add Client", e);
        } catch (IOException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Cannot add Client", e);
        } finally {
            DBManager.closeStatement(ps);
            DBManager.closeConnection(conn);
        }
    }

    @Override
    public void deleteClient(long id) throws DAOException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            Object[] parameters = {id};
            ps = sqlStatements.buildSQLStatement(conn, DELETE_CLIENTE_KEY, parameters);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Cannot delete the client", e);
        } catch (IOException e) {
            throw new DAOException("Cannot delete the client", e);
        } finally {
            DBManager.closeStatement(ps);
            DBManager.closeConnection(conn);
        }
    }

    @Override
    public void updateClient(Client client) throws DAOException {
        Connection conn = null;
        PreparedStatement update = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            Object[] parameters = {
                client.getCellphone(),
                client.getNames(),
                client.getLastName()
            };
            update = sqlStatements.buildSQLStatement(conn, UPDATE_CLIENTE_KEY, parameters);
            update.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Could not properly update the client", e);
        } catch (IOException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Could not properly update the client", e);
        } finally {
            DBManager.closeStatement(update);
            DBManager.closeConnection(conn);
        }
    }

}
