/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import Conexion.ConexionMySQL;
import Entidades.Persona;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author nejo
 */
public class Personal extends ConexionMySQL {
    @Override
    public ArrayList getList(String query) {
        ArrayList<Persona> personaList = new ArrayList<Persona>();
        try {
            Statement st = (Statement) getCon().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Persona p = new Persona();
                p.setId(rs.getInt("id"));
                p.setApellido(rs.getString("apellido"));
                p.setNombres(rs.getString("nombres"));
                p.setDomicilio(rs.getString("domicilio"));
                p.setFecha_nac(rs.getDate("fecha_nac"));
                p.setNro_docu(rs.getInt("nro_docu"));
                p.setCuil(rs.getString("cuil"));
                p.setCelular1(rs.getInt("celular1"));
                p.setCelular2(rs.getInt("celular2"));
                p.setEmail(rs.getString("email"));
                p.setTipo_personal(rs.getString("tipo_personal"));
                personaList.add(p);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } finally {
            return personaList;
        }
    }

    @Override
    public boolean isIngresar(Object obj) {
        boolean ok = false;
        Persona p = (Persona) obj;
        try {
            Statement st = (Statement) getCon().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            String query = "SELECT * FROM personal WHERE id=0";

            ResultSet rs = st.executeQuery(query);

            rs.moveToInsertRow();

            rs.updateString("apellido", p.getApellido());
            rs.updateString("nombres", p.getNombres());
            rs.updateString("domicilio", p.getDomicilio());
            if (p.getFecha_nac() != null) {
                String patron1 = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(patron1);
                String mFechaNac = simpleDateFormat1.format(p.getFecha_nac());
                rs.updateString("fecha_nac", mFechaNac);
            } else {
                rs.updateDate("fecha_nac", null);
            }
            rs.updateInt("nro_docu", p.getNro_docu());
            rs.updateString("cuil", p.getCuil());
            rs.updateInt("celular1", p.getCelular1());
            rs.updateInt("celular2", p.getCelular2());
            rs.updateString("email", p.getEmail());
            rs.updateString("tipo_personal", p.getTipo_personal());

            rs.insertRow();
            rs.close();
            st.close();

            ok = true;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ok = false;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ok = false;
        } finally {
            return ok;
        }
    }

    @Override
    public boolean isEliminar(Object obj) {
        boolean ok = false;
        Persona p = (Persona) obj;
        try {
            Statement st = (Statement) getCon().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            String query = "SELECT * FROM personal WHERE id=" + p.getId();

            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                rs.deleteRow();
                ok = true;
            } else {
                ok = false;
            }

            rs.close();
            st.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ok = false;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ok = false;
        } finally {
            return ok;
        }
    }

    @Override
    public boolean isModificar(Object obj) {
        boolean ok = false;
        Persona p = (Persona) obj;
        try {
            Statement st = (Statement) getCon().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            String query = "SELECT * FROM personal WHERE id=" + p.getId();

            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                rs.updateString("apellido", p.getApellido());
                rs.updateString("nombres", p.getNombres());
                rs.updateString("domicilio", p.getDomicilio());
                if (p.getFecha_nac() != null) {
                    String patron1 = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(patron1);
                    String mFechaNac = simpleDateFormat1.format(p.getFecha_nac());
                    rs.updateString("fecha_nac", mFechaNac);
                } else {
                    rs.updateDate("fecha_nac", null);
                }
                rs.updateInt("nro_docu", p.getNro_docu());
                rs.updateString("cuil", p.getCuil());
                rs.updateInt("celular1", p.getCelular1());
                rs.updateInt("celular2", p.getCelular2());
                rs.updateString("email", p.getEmail());
                rs.updateString("tipo_personal", p.getTipo_personal());

                rs.updateRow();
                ok = true;
            } else {
                ok = false;
            }

            rs.close();
            st.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ok = false;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ok = false;
        } finally {
            return ok;
        }
    }

    @Override
    public boolean isActualizar(String update) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
