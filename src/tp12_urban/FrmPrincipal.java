/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tp12_urban;

import Datos.Personal;
import Entidades.Persona;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author nejo
 */
public class FrmPrincipal extends javax.swing.JFrame {

    private DefaultTableModel modelo = null;//nuevo modelo de tabla
    private TableRowSorter<TableModel> elQueOrdena = null;
    private TreeMap<String, Persona> listaPersonal = null;

    /**
     * Creates new form FrmPrincipal
     */
    
    //Variables para colocar la hora
    String hora, minutos, segundos;
    Thread hilo;
    
    
    public FrmPrincipal() {
        initComponents();
        _tabla();
        _cargarPersonal();
        Hora h = new Hora(horaLbl);
        h.start();
        jDatos.getTableHeader().setFont(new Font("MS Reference Sans Serif", Font.BOLD, 12));
        jDatos.getTableHeader().setOpaque(false);
        jDatos.getTableHeader().setBackground(new Color(32, 136, 203));
        jDatos.getTableHeader().setForeground(new Color(255, 255, 255));
        jDatos.setRowHeight(25);
        setIconImage(new ImageIcon(getClass().getResource("/tp12_urban/java_coffee_13071.png")).getImage());
    }

    //Configuracion de la tabla, de los datos mas importantes a mostrar
    public void _tabla() {
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("NOMBRES");
        modelo.addColumn("DOCUMENTO");
        modelo.addColumn("CARGO/PUESTO");

        elQueOrdena = new TableRowSorter<TableModel>(modelo);
        jDatos.setModel(modelo);
        jDatos.setRowSorter(elQueOrdena);

        jDatos.getColumnModel().getColumn(0).setPreferredWidth(60);
        jDatos.getColumnModel().getColumn(1).setPreferredWidth(120);
        jDatos.getColumnModel().getColumn(2).setPreferredWidth(140);
        jDatos.getColumnModel().getColumn(3).setPreferredWidth(100);
        jDatos.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    //Limpia la tabla
    private void _limpiarModelo() {
        if (modelo != null) {
            modelo.setRowCount(0);
        }
    }

    //Vacia los campos de datos
    private void _limpiar() {
        apellidoTxt.setText("");
        nombresTxt.setText("");
        domicilioTxt.setText("");
        fecha_nac.setCalendar(null);
        nroDocumentoTxt.setText("");
        cuilTxt.setText("");
        celular1Txt.setText("");
        celular2Txt.setText("");
        emailTxt.setText("");
        tipoPersonal.setSelectedIndex(0);
    }

    //Carga de personal en treemap
    public void _cargarPersonal() {
        _limpiarModelo();
        Personal cnx = new Personal();

        ArrayList<Persona> list = new ArrayList<Persona>();

        if (cnx.conectar()) {
            list = cnx.getList("SELECT * FROM personal");
        }

        listaPersonal = new TreeMap<String, Persona>();

        for (int index = 0; index < list.size(); index++) {
            Persona p = list.get(index);
            modelo.addRow(p.getInfo());

            String key = p.getId() + "";
            listaPersonal.put(key, p);
        }
    }

    public void _seleccionar() {
        String key = "";
        Persona p = null;
        try {
            int indexTable = jDatos.getSelectedRow();
            int indexModelo = jDatos.convertRowIndexToModel(indexTable);

            key = modelo.getValueAt(indexModelo, 0).toString();
            if (listaPersonal.containsKey(key)) {
                p = listaPersonal.get(key);
            }
        } catch (Exception ex) {
        } finally {
            _mostrarPersonal(p);
        }
    }

    //Filtro de datos del personal
    public void _filtar(String texto) {
        if (elQueOrdena != null) {
            elQueOrdena.setRowFilter(RowFilter.regexFilter(texto.trim()));
        }
    }

    public void _mostrarPersonal(Persona per) {
        String id = "";
        String apellido = "";
        String nombres = "";
        String domicilio = "";
        Date fechaNac = null;
        String nroDoc = "0";
        String cuil = "";
        String celular1 = "";
        String celular2 = "";
        String email = "";
        String tipo_per = "";
        if (per != null) {
            id = per.getId() + "";
            apellido = per.getApellido().trim();
            nombres = per.getNombres().trim();
            domicilio = per.getDomicilio().trim();
            fechaNac = per.getFecha_nac();
            nroDoc = per.getNro_docu() + "";
            cuil = per.getCuil().trim();
            celular1 = per.getCelular1().toString();
            celular2 = per.getCelular2().toString();
            email = per.getEmail().trim();
            tipo_per = per.getTipo_personal().trim();
        }
        idTxt.setText(id);
        apellidoTxt.setText(apellido);
        nombresTxt.setText(nombres);
        domicilioTxt.setText(domicilio);
        fecha_nac.setDate(fechaNac);
        nroDocumentoTxt.setText(nroDoc);
        cuilTxt.setText(cuil);
        celular1Txt.setText(celular1);
        celular2Txt.setText(celular2);
        emailTxt.setText(email);
        tipoPersonal.setSelectedItem(tipo_per);
    }

    //Funcion con una expresion para saber si solo contiene letras.
    public static boolean isAlpha(String a) {
        return a != null && a.matches("^[a-zA-Z\\s]*$");
    }

    //Funcion para validar que sea un correo electronico.
    public static boolean esCorreo(String a) {
        return a != null && a.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    //Validacion de personal que se agrega
    private Object[] _validar() {
        boolean ok = false;
        Persona p = new Persona();

        Object[] oResult = new Object[2];
        oResult[0] = ok;

        String apellido = apellidoTxt.getText().trim();
        String nombres = nombresTxt.getText().trim();
        String domicilio = domicilioTxt.getText().trim();
        String documento = nroDocumentoTxt.getText().trim();
        int documentoValido = 0;
        String cuil = cuilTxt.getText().trim();
        String celular1 = celular1Txt.getText().trim();
        String celular2 = celular2Txt.getText().trim();
        String email = emailTxt.getText().trim();
        String emailValido = "";
        Integer celular1Valido = 0;
        Integer celular2Valido = 0;

        p.setFecha_nac(fecha_nac.getDate());
        p.setCuil(cuilTxt.getText().trim());
        p.setTipo_personal(tipoPersonal.getSelectedItem().toString());

        try {
            if (idTxt.getText().trim().isEmpty()) {
                p.setId(0);
            } else {
                p.setId(Integer.parseInt(idTxt.getText()));
            }
            p.setApellido(apellidoTxt.getText().trim());
            p.setNombres(nombresTxt.getText().trim());
        } catch (Exception ex) {
        }

        //Validacion del apellido
        if (isAlpha(apellido)) {
            apellido = (apellido.substring(0, 1).toUpperCase() + apellido.substring(1).toLowerCase());
            p.setApellido(apellido);
            ok = true;
        } else {
            JOptionPane.showMessageDialog(agregarBtn, "¡Para ingresar el APELLIDO solo se permiten letras!", "¡Atencion!", JOptionPane.WARNING_MESSAGE);
            apellidoTxt.setText("");
            apellidoTxt.requestFocus();
            ok = false;
            oResult[0] = ok;
            return oResult;
        }

        //Validacion del nombre
        if (isAlpha(nombres)) {
            nombres = (nombres.substring(0, 1).toUpperCase() + nombres.substring(1).toLowerCase());
            p.setNombres(nombres);
            ok = true;
        } else {
            JOptionPane.showMessageDialog(agregarBtn, "¡Para ingresar el NOMBRE solo se permiten letras!", "¡Atencion!", JOptionPane.WARNING_MESSAGE);
            nombresTxt.setText("");
            nombresTxt.requestFocus();
            ok = false;
            oResult[0] = ok;
            return oResult;
        }

        //Validacion del domicilio
        if (isAlpha(domicilio)) {
            domicilio = (domicilio.substring(0, 1).toUpperCase() + domicilio.substring(1).toLowerCase());
            p.setDomicilio(domicilio);
            ok = true;
        } else {
            JOptionPane.showMessageDialog(agregarBtn, "¡Para ingresar el DOMICILIO solo se permiten letras!", "¡Atencion!", JOptionPane.WARNING_MESSAGE);
            domicilioTxt.setText("");
            domicilioTxt.requestFocus();
            ok = false;
            oResult[0] = ok;
            return oResult;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String date = sdf.format(fecha_nac.getDate());

        //Se valida que una fecha este colocada en el calendario
        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(agregarBtn, "¡Seleccione una fecha en el calendario!", "Atencion", JOptionPane.WARNING_MESSAGE);
        }

        //Validacion del documento
        try {
            documento.chars().allMatch(Character::isDigit);
            //Transforma el dato de string a int.
            documentoValido = Integer.parseInt(nroDocumentoTxt.getText().trim());
            p.setNro_docu(documentoValido);
            ok = true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(agregarBtn, "¡Para ingresar el DOCUMENTO solo se permiten números!", "Atencion", JOptionPane.WARNING_MESSAGE);
            nroDocumentoTxt.setText("");
            nroDocumentoTxt.requestFocus();
            ok = false;
            oResult[0] = ok;
            return oResult;
        }

        //Validacion del celular1
        try {
            celular1.chars().allMatch(Character::isDigit);
            //Transforma el dato de string a int.
            celular1Valido = Integer.parseInt(celular1Txt.getText().trim());
            p.setCelular1(celular1Valido);
            ok = true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(agregarBtn, "¡Para ingresar el CELULAR 1 solo se permiten números!", "Atencion", JOptionPane.WARNING_MESSAGE);
            celular1Txt.setText("");
            celular1Txt.requestFocus();
            ok = false;
            oResult[0] = ok;
            return oResult;
        }

        if (!celular2.isEmpty()) {
            try {
                celular2.chars().allMatch(Character::isDigit);
                //Transforma el dato de string a int.
                celular2Valido = Integer.parseInt(celular2Txt.getText().trim());
                p.setCelular2(celular2Valido);
                ok = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(agregarBtn, "¡Para ingresar el CELULAR 2 solo se permiten números!", "Atencion", JOptionPane.WARNING_MESSAGE);
                celular2Txt.setText("");
                celular2Txt.requestFocus();
                ok = false;
                oResult[0] = ok;
                return oResult;
            }
        }

        if (celular2.isEmpty()) {
            JOptionPane.showMessageDialog(agregarBtn, "¡Debe ingresar un segundo celular!", "Atencion", JOptionPane.WARNING_MESSAGE);
            celular2Txt.setText("");
            celular2Txt.requestFocus();
            ok = false;
            oResult[0] = ok;
            return oResult;
        }

        //Valida que el correo ingresado sea uno valido
        if (esCorreo(email)) {
            emailValido = email;
            p.setEmail(emailValido);
            ok = true;
        } else { //Si no, arroja un mensaje, deja el campo vacio y recupera el foco
            JOptionPane.showMessageDialog(agregarBtn, "¡El CORREO no es valido!", "¡Atención!", JOptionPane.WARNING_MESSAGE);
            emailTxt.setText("");
            emailTxt.requestFocus();
            ok = false;
            oResult[0] = ok;
            return oResult;
        }

        Object[] oReturn = new Object[2];
        oReturn[0] = ok;
        oReturn[1] = p;
        return oReturn;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        filtroTxt = new javax.swing.JTextField();
        horaLbl = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        agregarBtn = new javax.swing.JButton();
        editarBtn = new javax.swing.JButton();
        eliminarBtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        apellidoTxt = new javax.swing.JTextField();
        nombresTxt = new javax.swing.JTextField();
        domicilioTxt = new javax.swing.JTextField();
        nroDocumentoTxt = new javax.swing.JTextField();
        cuilTxt = new javax.swing.JTextField();
        celular1Txt = new javax.swing.JTextField();
        celular2Txt = new javax.swing.JTextField();
        emailTxt = new javax.swing.JTextField();
        tipoPersonal = new javax.swing.JComboBox<>();
        idTxt = new javax.swing.JTextField();
        fecha_nac = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jDatos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TP N°12 - URBAN DANIEL");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setPreferredSize(new java.awt.Dimension(1042, 65));

        filtroTxt.setBackground(new java.awt.Color(255, 255, 255));
        filtroTxt.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        filtroTxt.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "FILTRAR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N
        filtroTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtroTxtKeyReleased(evt);
            }
        });

        horaLbl.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 18)); // NOI18N
        horaLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filtroTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(horaLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(horaLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(filtroTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 12, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DATOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 14))); // NOI18N
        jPanel2.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(700, 652));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(32, 136, 203));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.setMinimumSize(new java.awt.Dimension(208, 50));
        jPanel4.setPreferredSize(new java.awt.Dimension(273, 60));

        agregarBtn.setBackground(new java.awt.Color(255, 255, 255));
        agregarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/add1.png"))); // NOI18N
        agregarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarBtnActionPerformed(evt);
            }
        });
        jPanel4.add(agregarBtn);

        editarBtn.setBackground(new java.awt.Color(255, 255, 255));
        editarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/modify.png"))); // NOI18N
        editarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editarBtnMouseClicked(evt);
            }
        });
        jPanel4.add(editarBtn);

        eliminarBtn.setBackground(new java.awt.Color(255, 255, 255));
        eliminarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/remove.png"))); // NOI18N
        eliminarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eliminarBtnMouseClicked(evt);
            }
        });
        eliminarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarBtnActionPerformed(evt);
            }
        });
        jPanel4.add(eliminarBtn);

        jPanel2.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        apellidoTxt.setBackground(new java.awt.Color(255, 255, 255));
        apellidoTxt.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        apellidoTxt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Apellido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N

        nombresTxt.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        nombresTxt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Nombres", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N

        domicilioTxt.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        domicilioTxt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Domicilio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N

        nroDocumentoTxt.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        nroDocumentoTxt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Nro Documento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N

        cuilTxt.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        cuilTxt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)), "Cuil", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N

        celular1Txt.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        celular1Txt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Celular 1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N

        celular2Txt.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        celular2Txt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Celular 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N

        emailTxt.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        emailTxt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Email", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N

        tipoPersonal.setBackground(new java.awt.Color(255, 255, 255));
        tipoPersonal.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        tipoPersonal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gerente", "Recepcionista", "Limpieza" }));
        tipoPersonal.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Tipo de Personal", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N

        idTxt.setEditable(false);
        idTxt.setBackground(new java.awt.Color(255, 255, 255));
        idTxt.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        idTxt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "ID", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N
        idTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTxtActionPerformed(evt);
            }
        });

        fecha_nac.setBackground(new java.awt.Color(255, 255, 255));
        fecha_nac.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Fecha de nacimiento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 12))); // NOI18N
        fecha_nac.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(apellidoTxt)
                    .addComponent(domicilioTxt)
                    .addComponent(emailTxt)
                    .addComponent(nroDocumentoTxt)
                    .addComponent(celular1Txt, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                .addGap(61, 61, 61)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nombresTxt)
                    .addComponent(tipoPersonal, 0, 258, Short.MAX_VALUE)
                    .addComponent(cuilTxt)
                    .addComponent(celular2Txt)
                    .addComponent(fecha_nac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(48, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(299, 299, 299))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apellidoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombresTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(domicilioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_nac, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nroDocumentoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cuilTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(celular1Txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(celular2Txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tipoPersonal, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_END);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TABLA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Reference Sans Serif", 1, 14))); // NOI18N
        jPanel3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N

        jDatos.setBackground(new java.awt.Color(255, 255, 255));
        jDatos.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        jDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jDatos.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jDatos.setShowVerticalLines(false);
        jDatos.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jDatosAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDatosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jDatos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agregarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarBtnActionPerformed
        Object[] oReturn = _validar();
        boolean ok = (boolean) oReturn[0];
        if (ok) {
            Persona pNuevo = (Persona) oReturn[1];
            Personal cnx = new Personal();
            if (cnx.conectar()) {
                ok = cnx.isIngresar(pNuevo);
                _limpiar();
            }
            if (ok) {
                _cargarPersonal();
                _mostrarPersonal(new Persona());
            } else {
                JOptionPane.showMessageDialog(jPanel2, "No se pudo cargar los datos", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(jPanel2, "Verifique los datos", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_agregarBtnActionPerformed

    private void editarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editarBtnMouseClicked
        //Boton para modificar datos
        int index = jDatos.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Para modificar un registro, seleccionelo.", "¡Atención!", JOptionPane.WARNING_MESSAGE);
        } else {
            Object[] oReturn = _validar();
            boolean ok = (boolean) oReturn[0];
            if (ok) {
                Persona pModif = (Persona) oReturn[1];
                String msj = "¿Desea modificar los datos de " + pModif.getApellido().trim() + " " + pModif.getNombres().trim() + "?";
                if (JOptionPane.showConfirmDialog(jPanel2, msj, "Atención", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Personal cnx = new Personal();
                    if (cnx.conectar()) {
                        ok = cnx.isModificar(pModif);
                        JOptionPane.showMessageDialog(jPanel2, "Registro modificado con exito!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                    if (ok) {
                        _cargarPersonal();
                    } else {
                        JOptionPane.showMessageDialog(jPanel2, "No se pudo actualizar!", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(jPanel2, "Controle los datos!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_editarBtnMouseClicked

    private void eliminarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarBtnMouseClicked

    }//GEN-LAST:event_eliminarBtnMouseClicked

    private void jDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDatosMouseClicked
        _seleccionar();
    }//GEN-LAST:event_jDatosMouseClicked

    private void jDatosAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jDatosAncestorAdded
        _seleccionar();
    }//GEN-LAST:event_jDatosAncestorAdded

    private void eliminarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBtnActionPerformed
        //Boton para eliminar datos, se debe seleccionar un registro
        int index = jDatos.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Para eliminar un registro, seleccionelo", "¡Atención!", JOptionPane.WARNING_MESSAGE);
        } else {
            Object[] oReturn = _validar();
            boolean ok = (boolean) oReturn[0];
            if (ok) {
                Persona pElim = (Persona) oReturn[1];
                String msj = "¿Desea eliminar a " + pElim.getApellido().trim() + " " + pElim.getNombres().trim() + "?";
                if (JOptionPane.showConfirmDialog(jPanel2, msj, "Atención", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                    Personal cnx = new Personal();
                    if (cnx.conectar()) {
                        ok = cnx.isEliminar(pElim);
                        JOptionPane.showMessageDialog(jPanel2, "Registro eliminado con exito", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                    if (ok) {
                        _cargarPersonal();
                    } else {
                        JOptionPane.showMessageDialog(jPanel2, "No se elimino al usuario", "Error", JOptionPane.WARNING_MESSAGE);
                    }

                }
            } else {
                JOptionPane.showMessageDialog(jPanel2, "Controle los datos", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_eliminarBtnActionPerformed

    private void filtroTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filtroTxtKeyReleased
        _filtar(filtroTxt.getText().trim());
    }//GEN-LAST:event_filtroTxtKeyReleased

    private void idTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idTxtActionPerformed

    /**
         * @param args the command line arguments
         */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarBtn;
    private javax.swing.JTextField apellidoTxt;
    private javax.swing.JTextField celular1Txt;
    private javax.swing.JTextField celular2Txt;
    private javax.swing.JTextField cuilTxt;
    private javax.swing.JTextField domicilioTxt;
    private javax.swing.JButton editarBtn;
    private javax.swing.JButton eliminarBtn;
    private javax.swing.JTextField emailTxt;
    private com.toedter.calendar.JDateChooser fecha_nac;
    private javax.swing.JTextField filtroTxt;
    private javax.swing.JLabel horaLbl;
    private javax.swing.JTextField idTxt;
    private javax.swing.JTable jDatos;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nombresTxt;
    private javax.swing.JTextField nroDocumentoTxt;
    private javax.swing.JComboBox<String> tipoPersonal;
    // End of variables declaration//GEN-END:variables
}
