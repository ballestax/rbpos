
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb.persistence.JDBC;

import com.rb.DBManager;
import com.rb.domain.Pay;
import com.rb.persistence.SQLExtractor;
import com.rb.persistence.SQLLoader;
import com.rb.persistence.dao.PayDAO;
import com.rb.persistence.dao.DAOException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author LuisR
 */
public class JDBCPayDAO implements PayDAO {

    public static final String TABLE_NAME = "pays";
    public static final String NAMED_PARAM_WHERE = "{where}";
    public static final String NAMED_PARAM_ORDER_BY = "{orderby}";
    private static final Logger logger = Logger.getLogger(JDBCPayDAO.class.getCanonicalName());
    private final DataSource dataSource;
    private final SQLLoader sqlStatements;
    protected static final String CREATE_PAYS_TABLE_KEY = "CREATE_PAYS_TABLE";
    protected static final String INSERT_PAY_KEY = "ADD_PAY";
    protected static final String UPDATE_PAY_KEY = "UPDATE_PAY";
    protected static final String GET_PAY_KEY = "GET_PAYS";
    protected static final String DELETE_PAY_KEY = "DELETE_PAY";
    protected static final String GET_PAY_BY_INVOICE_KEY = "GET_PAY_BY_INVOICE";

    public JDBCPayDAO(BasicDataSource dataSource, SQLLoader sqlStatements) throws DAOException {
        this.dataSource = dataSource;
        this.sqlStatements = sqlStatements;
    }

    public final void init() throws DAOException, RemoteException {
        // Create the pays table if it does not already exist
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            if (DBManager.tableExists(TABLE_NAME, conn)) {
                return;
            }
            ps = sqlStatements.buildSQLStatement(conn, CREATE_PAYS_TABLE_KEY);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Cannot create Payes table", e);
        } catch (IOException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Cannot create Payes table", e);
        } finally {
            DBManager.closeStatement(ps);
            DBManager.closeConnection(conn);
        }
    }

    public Pay getPayBy(String query) throws DAOException {
        String retrieveImporter;
        try {
            SQLExtractor sqlExtractorWhere = new SQLExtractor(query, SQLExtractor.Type.WHERE);
            Map<String, String> namedParams = new HashMap<String, String>();
            namedParams.put(NAMED_PARAM_WHERE, sqlExtractorWhere.extractWhere());
            retrieveImporter = sqlStatements.getSQLString(GET_PAY_KEY, namedParams);

        } catch (SQLException e) {
            throw new DAOException("Could not properly retrieve the Pay", e);
        } catch (IOException e) {
            throw new DAOException("Could not properly retrieve the Pay", e);
        }
        Connection conn = null;
        PreparedStatement retrieve = null;
        ResultSet rs = null;
        Pay pay = null;
        try {
            conn = dataSource.getConnection();
            retrieve = conn.prepareStatement(retrieveImporter);
            rs = retrieve.executeQuery();
            while (rs.next()) {
                pay = new Pay();
                pay.setId(rs.getInt("id"));
                pay.setCodigo("P" + rs.getInt("id"));
                pay.setCycle(rs.getLong("cycle"));
                pay.setFecha(rs.getDate("createdTime"));
                pay.setIdInvoice(rs.getInt("id_invoice"));
                pay.setEfecty(rs.getBigDecimal("efecty"));
                pay.setTransfer(rs.getBigDecimal("transfer"));
                pay.setCard(rs.getBigDecimal("card"));
                pay.setCambio(rs.getBigDecimal("cambio"));
                pay.setCredit(rs.getBigDecimal("credit"));
                pay.setEfecty(rs.getBigDecimal("efecty"));
                pay.setCardType(rs.getInt("card_type"));
                pay.setType(rs.getInt("pay_type"));
                pay.setBankTransfer(rs.getString("bank_transfer"));
                pay.setUpdateTime(rs.getDate("lastUpdateTime"));
                pay.setUser(rs.getString("user"));
            }
        } catch (SQLException e) {
            throw new DAOException("Could not properly retrieve the pay: " + e);
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closeStatement(retrieve);
            DBManager.closeConnection(conn);
        }
        return pay;
    }

    @Override
    public Pay getPay(int id) throws DAOException {
        return getPayBy("id=" + id);
    }

    public Pay getPayByInvoice(String factura) throws DAOException {
        Pay pay = null;
        Connection conn = null;
        PreparedStatement retrieve = null;
        ResultSet rs = null;
        Object[] parameters = {factura};
        try {
            conn = dataSource.getConnection();
            retrieve = sqlStatements.buildSQLStatement(conn, GET_PAY_BY_INVOICE_KEY, parameters);

            rs = retrieve.executeQuery();
            while (rs.next()) {
                pay = new Pay();
                pay.setId(rs.getInt("id"));
                pay.setCodigo("P" + rs.getInt("id"));
                pay.setCycle(rs.getLong("cycle"));
                pay.setFecha(rs.getDate("createdTime"));
                pay.setIdInvoice(rs.getInt("id_invoice"));
                pay.setEfecty(rs.getBigDecimal("efecty"));
                pay.setTransfer(rs.getBigDecimal("transfer"));
                pay.setCard(rs.getBigDecimal("card"));
                pay.setCambio(rs.getBigDecimal("cambio"));
                pay.setCredit(rs.getBigDecimal("credit"));
                pay.setEfecty(rs.getBigDecimal("efecty"));
                pay.setCardType(rs.getInt("card_type"));
                pay.setType(rs.getInt("pay_type"));
                pay.setBankTransfer(rs.getString("bank_transfer"));
                pay.setUpdateTime(rs.getDate("lastUpdateTime"));
                pay.setUser(rs.getString("user"));
            }
        } catch (SQLException e) {
            throw new DAOException("Could not properly retrieve the pay: " + e);
        } catch (IOException e) {
            throw new DAOException("Could not properly retrieve the pay: " + e);
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closeStatement(retrieve);
            DBManager.closeConnection(conn);
        }
        return pay;
    }

    @Override
    public ArrayList<Pay> getPayList() throws DAOException {
        return getPayList("", "");
    }

    public ArrayList<Pay> getPayList(String where, String orderBy) throws DAOException {
        String retrieveCategory;
        ArrayList<Pay> payes = new ArrayList<>();
        try {
            SQLExtractor sqlExtractorWhere = new SQLExtractor(where, SQLExtractor.Type.WHERE);;
            SQLExtractor sqlExtractorOrderBy = new SQLExtractor(orderBy, SQLExtractor.Type.ORDER_BY);;
            Map<String, String> namedParams = new HashMap<>();
            namedParams.put(NAMED_PARAM_WHERE, sqlExtractorWhere.extractWhere());
            namedParams.put(NAMED_PARAM_ORDER_BY, sqlExtractorOrderBy.extractOrderBy());
            retrieveCategory = sqlStatements.getSQLString(GET_PAY_KEY, namedParams);

        } catch (SQLException e) {
            throw new DAOException("Could not properly retrieve the Pay List", e);
        } catch (IOException e) {
            throw new DAOException("Could not properly retrieve the Pay List", e);
        }

        Connection conn = null;
        PreparedStatement retrieve = null;
        ResultSet rs = null;
        Pay pay = null;
        try {
            conn = dataSource.getConnection();
            retrieve = conn.prepareStatement(retrieveCategory);
            rs = retrieve.executeQuery();

            while (rs.next()) {
                pay = new Pay();
                pay.setId(rs.getInt("id"));
                pay.setCodigo("P" + rs.getInt("id"));
                pay.setCycle(rs.getLong("cycle"));
                pay.setFecha(rs.getDate("createdTime"));
                pay.setIdInvoice(rs.getInt("id_invoice"));
                pay.setEfecty(rs.getBigDecimal("efecty"));
                pay.setTransfer(rs.getBigDecimal("transfer"));
                pay.setCard(rs.getBigDecimal("card"));
                pay.setCambio(rs.getBigDecimal("cambio"));
                pay.setCredit(rs.getBigDecimal("credit"));
                pay.setEfecty(rs.getBigDecimal("efecty"));
                pay.setCardType(rs.getInt("card_type"));
                pay.setType(rs.getInt("pay_type"));
                pay.setBankTransfer(rs.getString("bank_transfer"));
                pay.setUpdateTime(rs.getDate("lastUpdateTime"));
                pay.setUser(rs.getString("user"));
                payes.add(pay);
            }
        } catch (SQLException e) {
            throw new DAOException("Could not proper retrieve the Pay: " + e);
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closeStatement(retrieve);
            DBManager.closeConnection(conn);
        }
        return payes;
    }

    @Override
    public long addPay(Pay pay) throws DAOException {
        if (pay == null) {
            throw new IllegalArgumentException("Null pay");
        }
        long idPay = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            Object[] parameters = {
                pay.getIdInvoice(),
                pay.getCycle(),
                pay.getType().getCode(),
                pay.getEfecty(),
                pay.getTransfer(),
                pay.getBankTransfer(),
                pay.getCard(),
                pay.getCardType(),
                pay.getCambio(),
                pay.getCredit(),
                pay.getUser()
            };
            ps = sqlStatements.buildSQLStatement(conn, INSERT_PAY_KEY, parameters, Statement.RETURN_GENERATED_KEYS);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet genK = ps.getGeneratedKeys();
                if (genK.next()) {
                    idPay = genK.getInt(1);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Cannot add pay", e);
        } catch (IOException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Cannot add pay", e);
        } finally {
            DBManager.closeStatement(ps);
            DBManager.closeConnection(conn);
        }
        return idPay;
    }

    @Override
    public void deletePay(int id) throws DAOException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            Object[] parameters = {id};
            ps = sqlStatements.buildSQLStatement(conn, DELETE_PAY_KEY, parameters);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Cannot delete the pay", e);
        } catch (IOException e) {
            throw new DAOException("Cannot delete the pay", e);
        } finally {
            DBManager.closeStatement(ps);
            DBManager.closeConnection(conn);
        }
    }

    @Override
    public void updatePay(Pay pay) throws DAOException {
        Connection conn = null;
        PreparedStatement update = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            Object[] parameters = {
                pay.getIdInvoice(),
                pay.getCycle(),
                pay.getType().getCode(),
                pay.getEfecty(),
                pay.getTransfer(),
                pay.getBankTransfer(),
                pay.getCard(),
                pay.getCardType(),
                pay.getCambio(),
                pay.getCredit(),
                pay.getUser(),
                pay.getId()
            };
            update = sqlStatements.buildSQLStatement(conn, UPDATE_PAY_KEY, parameters);
            update.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Could not properly update the pay", e);
        } catch (IOException e) {
            DBManager.rollbackConn(conn);
            throw new DAOException("Could not properly update the pay", e);
        } finally {
            DBManager.closeStatement(update);
            DBManager.closeConnection(conn);
        }
    }

}
