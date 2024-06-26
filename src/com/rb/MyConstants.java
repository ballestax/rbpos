/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb;

import com.rb.domain.Permission;
import java.text.SimpleDateFormat;

/**
 *
 * @author hp
 */
public class MyConstants {

    public static final String FIELD_ID = "id";

    public static final String[] MONTHS = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO",
        "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};

    public static final int FILTER_TEXT_INT_EQUALS = 1;
    public static final int FILTER_TEXT_INT_START = 2;
    public static final int FILTER_TEXT_INT_CONTAINS = 3;
    public static final int FILTER_NUM_INT_EQUALS = 4;
    public static final int FILTER_NUM_INT_GREATER = 5;
    public static final int FILTER_NUM_INT_GREATER_EQUAL = 6;
    public static final int FILTER_NUM_INT_LESS = 7;
    public static final int FILTER_NUM_INT_LESS_EQUAL = 8;
    public static final int FILTER_NUM_INT_DIFFERENT = 9;

    public static final String FILTER_TEXT_EQUALS = "LIKE '<>'";
    public static final String FILTER_TEXT_START = "LIKE '<>%'";
    public static final String FILTER_TEXT_CONTAINS = "LIKE '%<>%'";
    public static final String FILTER_NUM_EQUALS = "=";
    public static final String FILTER_NUM_GREATER = ">";
    public static final String FILTER_NUM_GREATER_EQUAL = ">=";
    public static final String FILTER_NUM_LESS = "<";
    public static final String FILTER_NUM_LESS_EQUAL = "<=";
    public static final String FILTER_NUM_DIFFERENT = "<>";
    public static final String FILTER_BOOL_EQUAL = "=";
    public static final String FILTER_BOOL_DIFFERENT = "!=";

    public static final String[] FILTROS_TEXTO = {"IGUAL A", "EMPIEZA POR", "CONTIENE"};
    public static final String[] FILTROS_NUMERO = {"=", ">", ">=", "<", "<=", "<>"};
    public static final String[] FILTROS_BOOLEANOS = {"=", "<>"};

    public static final String PERM_SHOW_TAB_WAITERS = "show-tab-waiters";
    public static final String PERM_SHOW_TAB_TABLES = "show-tab-tables";
    public static final String PERM_SHOW_TAB_BACKUP = "show-tab-backup";
    public static final String PERM_SHOW_TAB_CONFIG = "show-tab-config";
    public static final String PERM_SHOW_TAB_USERS = "show-tab-users";
    public static final String PERM_SHOW_TAB_STATIONS = "show-tab-stations";

    public static final String PERM_ADMIN_MODULE = "show-admin-module";
    public static final String PERM_ORDERS_MODULE = "show-orders-module";
    public static final String PERM_CASH_MODULE = "show-cash-module";
    public static final String PERM_REPORTS_MODULE = "show-reports-module";
    public static final String PERM_SALES_MODULE = "show-sales-module";
    public static final String PERM_ORDERLIST_MODULE = "show-orderlist-module";
    public static final String PERM_INVENTORY_MODULE = "show-inventory-module";
    public static final String PERM_PRODUCTS_MODULE = "show-products-module";
    public static final String PERM_ANULATE_INVOICE = "allow-anulate-invoice";
    public static final String PERM_MOD_ORDER = "allow-mod-order";
    public static final String PERM_PRINT_INVOICE = "allow-print-ticket";
    public static final String PERM_OPEN_CASH = "allow-open-cash";

    public static final Permission[] PERMISSIONS = {
        new Permission(PERM_ANULATE_INVOICE, "Anular factura"),
        new Permission(PERM_MOD_ORDER, "Modificar orden"),
        new Permission(PERM_PRINT_INVOICE, "Omprimir ticket"),
        new Permission(PERM_ADMIN_MODULE, "Ver el modulo administrador"),
        new Permission(PERM_ORDERS_MODULE, "Ver el modulo pedidos"),
        new Permission(PERM_CASH_MODULE, "Ver el modulo caja"),
        new Permission(PERM_REPORTS_MODULE, "Ver el modulo reportes"),
        new Permission(PERM_SALES_MODULE, "Ver el modulo ventas"),
        new Permission(PERM_ORDERLIST_MODULE, "Ver el modulo lista de pedidos"),
        new Permission(PERM_SHOW_TAB_BACKUP, "Ver pestaña copia de seguridad"),
        new Permission(PERM_SHOW_TAB_CONFIG, "Ver pestaña configuracion"),
        new Permission(PERM_SHOW_TAB_USERS, "Ver el modulo usuarios"),
        new Permission(PERM_INVENTORY_MODULE, "Ver el modulo inventario"),
        new Permission(PERM_PRODUCTS_MODULE, "Ver el modulo productos"),
        new Permission("add-items-inventary", "Agregar items al inventario"),
        new Permission("load-items-inventary", "Cargar items al inventario"),
        new Permission("download-items-inventary", "Descargar items al inventario"),
        new Permission("conciliate-items-inventary", "Coniciliar items al inventario"),
        new Permission("print-items-inventary", "Inprimir items del inventario"),
        new Permission("export-items-inventary", "Exportar items del inventario"),
        new Permission("show-pos-module", "Ver el modulo POS"),
        new Permission("allow-cancel-product-order", "Cancelar producto en una orden"),
        new Permission("allow-modify-product-order", "Modificar producto en una orden"),
        new Permission(PERM_SHOW_TAB_WAITERS, "Ver pestaña meseros"),
        new Permission(PERM_SHOW_TAB_TABLES, "Ver pestaña mesas"),
        new Permission(PERM_SHOW_TAB_STATIONS, "Ver pestaña estaciones"),
        new Permission("allow-modify-waiters", "Modificar meseros"),
        new Permission("allow-modify-tables", "Modificar mesas"),
        new Permission("allow-modify-stations", "Modificar estaciones"),
        new Permission(PERM_OPEN_CASH, "Abrir caja"),};

    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("dd-MM-yyyy");

    public static final String PEDIDO_LOCAL = "LOCAL";
    public static final String PEDIDO_DOMICILIO = "DOMICILIO";
    public static final String PEDIDO_PARA_LLEVAR = "PARA LLEVAR";

    public static final String[] TIPO_PEDIDO = {PEDIDO_LOCAL, PEDIDO_DOMICILIO, PEDIDO_PARA_LLEVAR};

    public static final String[] PERIODOS = {"DIA", "SEMANA", "MES", "AÑO", "RANGO"};
    public static final String[] TIPO_REPORTE = {"PRODUCTOS VENDIDOS", "VENTAS"};

    public static final String CF_FACTURA_FINAL = "factura_final";
    public static final String CF_FACTURA_INICIAL = "factura_inicial";
    public static final String CF_FACTURA_ACTUAL = "factura_actual";

}
