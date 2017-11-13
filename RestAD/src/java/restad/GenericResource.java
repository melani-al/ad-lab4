/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.jws.WebParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
/**
 * REST Web Service
 *
 * @author Celina
 */
@Path("generic")
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     * Retrieves representation of an instance of restad.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHtml() {
        return "<html><head/><body><h1>Hello World!</h1><body></html>";
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }
    
    /**
    * @param id
    * @param fecha
    * @return
    */
    @Path("reserva")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public String reserva (@FormParam("id") String id, @FormParam("fecha") String fecha) {
        return "<html><head></head> <body> Reserva </body></html>";
    }
    
    /**
     * @param id_vuelo
     * @param fecha
     * @return 
     */
    @Path("consulta_libres")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public String consulta_libres (@FormParam("id_vuelo") int id_vuelo, @FormParam("fecha") int fecha){
        Connection connection = null;
        Integer ret = -1;
        try                        
        {            
          // load the sqlite-JDBC driver using the current class loader
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/Celina/Documents/00 Documentos - Celinas MacBook Air/Uni/5.Auslandssemester/AD/Ejercisios/ad-lab3/dblab3");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Melani\\Desktop\\FIB\\TI\\AD\\lab3-ad\\dblab3");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            
            String query = "select * from vuelo_fecha where id_vuelo= ? and fecha = ?";
           
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id_vuelo);
            pstmt.setInt(2, fecha);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                ret = rs.getInt("num_plazas_max") - rs.getInt("num_plazas_ocupadas");
            }
        }
        catch(SQLException | ClassNotFoundException e)
        {
          System.err.println(e.getMessage());
        }   
        finally
        {
          try
          {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e)
          {
            // connection close failed.
            System.err.println(e.getMessage());
          }
        }   
        return ret.toString();
    }
    
    /**
     * @param id_vuelo
     * @param fecha
     * @return 
     */
    @Path("reserva_plaza")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public String reserva_plaza (@FormParam("id_vuelo") int id_vuelo, @FormParam("fecha") int fecha){
        Connection connection = null;
        Integer ret = -1;
        try                        
        {            
          // load the sqlite-JDBC driver using the current class loader
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/Celina/Documents/00 Documentos - Celinas MacBook Air/Uni/5.Auslandssemester/AD/Ejercisios/ad-lab3/dblab3");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Melani\\Desktop\\FIB\\TI\\AD\\lab3-ad\\dblab3");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            System.out.println("in try block");
            
            String query = "select * from vuelo_fecha where id_hotel= ? and fecha = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id_vuelo);
            pstmt.setInt(2, fecha);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                if ((rs.getInt("num_plazas_max") - rs.getInt("num_plazas_ocupadas")) > 0) {
                    ret = rs.getInt("num_plaza_ocupadas") + 1;
                    PreparedStatement ps = connection.prepareStatement("update hotel_fecha set num_plaza_ocupadas = num_plaza_ocupadas +1 where id_hotel= ?");
                    ps.setInt(1, id_vuelo);
                    ps.executeUpdate();
                }else{
                    ret = -1;
                }
            }
        }
        catch(SQLException | ClassNotFoundException e)
        {
          System.err.println(e.getMessage());
        }   
        finally
        {
          try
          {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e)
          {
            // connection close failed.
            System.err.println(e.getMessage());
          }
        }   
        //(ret == -1) return "<html><head/><body><h1>Error -1!</h1><br><h3>No plazas libres<h/3><body></html>";
        return ret.toString();
    }
    
    /**
     *
     * @param id_hotel
     * @param fecha
     * @return
     */
    @Path("consulta_libres_habitaciones")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public String consulta_libres_habitaciones (@FormParam("id_hotel") int id_hotel, @FormParam("fecha") int fecha) {
        Connection connection = null;
        Integer ret = 0;
        try                        
        {            
          // load the sqlite-JDBC driver using the current class loader
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/Celina/Documents/00 Documentos - Celinas MacBook Air/Uni/5.Auslandssemester/AD/Ejercisios/ad-lab3/dblab3");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Melani\\Desktop\\FIB\\TI\\AD\\lab3-ad\\dblab3");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            
            String query = "select * from hotel_fecha where id_hotel= ? and fecha = ?";
           
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id_hotel);
            pstmt.setInt(2, fecha);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                System.out.println("hello");
                ret = rs.getInt("num_hab_libres");
            }
        }
        catch(SQLException | ClassNotFoundException e)
        {
          System.err.println(e.getMessage());
        }   
        finally
        {
          try
          {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e)
          {
            // connection close failed.
            System.err.println(e.getMessage());
          }
        }   
        return ret.toString();
    }
    
    /**
     * Web service operation
     * @param id_hotel
     * @param fecha
     * @return 
     */
    @Path("reserva_habitacion")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public String reserva_habitacion(@FormParam("id_hotel") int id_hotel, @FormParam("fecha") int fecha) {
        Connection connection = null;
        Integer ret = -1;
        try                        
        {            
          // load the sqlite-JDBC driver using the current class loader
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/Celina/Documents/00 Documentos - Celinas MacBook Air/Uni/5.Auslandssemester/AD/Ejercisios/ad-lab3/dblab3");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Melani\\Desktop\\FIB\\TI\\AD\\lab3-ad\\dblab3");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            
            String query = "select * from hotel_fecha where id_hotel= ? and fecha = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id_hotel);
            pstmt.setInt(2, fecha);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                if (rs.getInt("num_hab_libres") > 0) {
                    ret = rs.getInt("num_hab_ocupadas") + 1;
                    PreparedStatement ps = connection.prepareStatement("update hotel_fecha set num_hab_ocupadas = num_hab_ocupadas +1 where id_hotel= ?");
                    ps.setInt(1, id_hotel);
                    ps.executeUpdate();
                    PreparedStatement ps2 = connection.prepareStatement("update hotel_fecha set num_hab_libres = num_hab_libres -1 where id_hotel= ?");
                    ps2.setInt(1, id_hotel);
                    ps2.executeUpdate();
                }
            }
        }
        catch(SQLException | ClassNotFoundException e)
        {
          System.err.println(e.getMessage());
        }   
        finally
        {
          try
          {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e)
          {
            // connection close failed.
            System.err.println(e.getMessage());
          }
        }   
        return ret.toString();
    }
    
    /**
     * Web service operation
     * @param id_hotel
     * @param fecha
     * @param cantidad
     * @return 
     */
    @Path("reserva_varias_habitacion")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public String reserva_varias_habitacion(@FormParam("id_hotel") int id_hotel, @FormParam("fecha") int fecha, @WebParam(name = "cantidad") Integer cantidad) {
        Connection connection = null;
        Integer ret = -1;
        try                        
        {            
          // load the sqlite-JDBC driver using the current class loader
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/Celina/Documents/00 Documentos - Celinas MacBook Air/Uni/5.Auslandssemester/AD/Ejercisios/ad-lab3/dblab3");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Melani\\Desktop\\FIB\\TI\\AD\\lab3-ad\\dblab3");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            
            String query = "select * from hotel_fecha where id_hotel= ? and fecha = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id_hotel);
            pstmt.setInt(2, fecha);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                if (rs.getInt("num_hab_libres") >= cantidad) {
                    ret = rs.getInt("num_hab_ocupadas") + cantidad;
                    PreparedStatement ps = connection.prepareStatement("update hotel_fecha set num_hab_ocupadas = num_hab_ocupadas +? where id_hotel= ?");
                    ps.setInt(1, cantidad);
                    ps.setInt(2, id_hotel);
                    ps.executeUpdate();
                    PreparedStatement ps2 = connection.prepareStatement("update hotel_fecha set num_hab_libres = num_hab_libres -? where id_hotel= ?");
                    ps2.setInt(1, cantidad);
                    ps2.setInt(2, id_hotel);
                    ps2.executeUpdate();
                }
            }
        }
        catch(SQLException | ClassNotFoundException e)
        {
          System.err.println(e.getMessage());
        }   
        finally
        {
          try
          {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e)
          {
            // connection close failed.
            System.err.println(e.getMessage());
          }
        }   
        return ret.toString();
    }

    /**
     *
     * @param id_vuelo
     * @param fecha
     * @param cantidad
     * @return
     */
    @Path("reserva_varias_plaza")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public String reserva_varias_plaza (@FormParam("id_vuelo") int id_vuelo, @FormParam("fecha") int fecha, @FormParam("cantidad") int cantidad){
        Connection connection = null;
        Integer ret = -1;
        try                        
        {            
          // load the sqlite-JDBC driver using the current class loader
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/Celina/Documents/00 Documentos - Celinas MacBook Air/Uni/5.Auslandssemester/AD/Ejercisios/ad-lab3/dblab3");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Melani\\Desktop\\FIB\\TI\\AD\\lab3-ad\\dblab3");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            
            String query = "select * from vuelo_fecha where id_hotel= ? and fecha = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id_vuelo);
            pstmt.setInt(2, fecha);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                if ((rs.getInt("num_plazas_max") - rs.getInt("num_plazas_ocupadas")) >= cantidad) {
                    ret = rs.getInt("num_plaza_ocupadas") + cantidad;
                    PreparedStatement ps = connection.prepareStatement("update hotel_fecha set num_plaza_ocupadas = num_plaza_ocupadas ? where id_hotel= ?");
                    ps.setInt(1, cantidad);
                    ps.setInt(2, id_vuelo);
                    ps.executeUpdate();
                }else{
                    ret = -1;
                }
            }
        }
        catch(SQLException | ClassNotFoundException e)
        {
          System.err.println(e.getMessage());
        }   
        finally
        {
          try
          {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e)
          {
            // connection close failed.
            System.err.println(e.getMessage());
          }
        }   
        //(ret == -1) return "<html><head/><body><h1>Error -1!</h1><br><h3>No plazas libres<h/3><body></html>";
        return ret.toString();
    }
    
}
