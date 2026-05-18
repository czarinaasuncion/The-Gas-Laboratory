// imports
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class TheGasLabGUI extends JFrame {
    // initialization ng mga java swing na gagamitin
    JFrame frame;
    JLabel lblGasImage, lblGasName, lblGasType, nobleBox, nonmetalBox, nobleTxt, nonmetalTxt;
    JPanel centerPanel, detailPanel, headerPanel, legendPanel, propertyPanel, appPanel, searchPanel;
    JButton btnBack, searchBtn;
    JTextArea txtDetails, txtApp;
    JTextField searchField;
    List<JButton> btnGas;
    
    // mga colors na gagamitin
    private final Color DarkBlue = new Color(42, 70, 116);
    private final Color LightPink = new Color(244, 219, 221);
    private final Color HeaderLineColor = new Color(173, 216, 230); // Light Blue separator
    private final Color Pink = new Color(246, 154, 162);
    private final Color Orange = new Color(252, 149, 79);

    // instantiate yung logic class
    TheGasLab logic = new TheGasLab();
    
    public TheGasLabGUI() {
        // para magrefer yung GUI sa logic
        logic.initializeSystem();

        // main window ito
        frame = new JFrame("The Gas Laboratory");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1200, 700));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        //top part ng window
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DarkBlue);
        headerPanel.setPreferredSize(new Dimension(0, 100));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, HeaderLineColor));
        // pag try inuutusan na try hanapin yung image, pag di mahanap or may error sa pic, pupunta sa catch
        // catch yung alternate pag di nagload yung image
        // ginagamit to when dealing with external sources, para in case may ma corrupt na file or mawala, may alternate
        try {
            // eto yung logo
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/gaslabGraphics/gaslablogo.png"));
            Image scaledLogo = logoIcon.getImage().getScaledInstance(110, 60, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(scaledLogo));
            lblLogo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            headerPanel.add(lblLogo, BorderLayout.WEST);
        } catch (Exception e) {
            JLabel lblLogoPlaceholder = new JLabel("  THE GAS LAB");
            lblLogoPlaceholder.setForeground(Color.WHITE);
            lblLogoPlaceholder.setFont(new Font("Arial", Font.BOLD, 22));
            headerPanel.add(lblLogoPlaceholder, BorderLayout.WEST);
        }

        // eto yung panel or area ng search
        searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 30));
        searchPanel.setOpaque(false);
        // eto kung san nagtatype for search
        searchField = new JTextField("Search Element", 10);
        searchField.setPreferredSize(new Dimension(150, 40));

        // eto search button
        searchBtn = new JButton("Find");
        searchBtn.setPreferredSize(new Dimension(80, 40));
        // ActionListener yung nagcoconnect sa button and logic, pag wala to di niya mapeperform function
        searchBtn.addActionListener(e -> performSearch());
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);

        // center panel, yung mismong periodic table map
        centerPanel = new JPanel(null);
        centerPanel.setBackground(DarkBlue);
        btnGas = new ArrayList<>(); //centerpanel kasi makikita btnGas or yung element blocks

        setupGasButtons();
        setupLegend();

        //detailpanel, dito lilitaw element details
        detailPanel = new JPanel(null);
        detailPanel.setPreferredSize(new Dimension(350, 0));
        detailPanel.setBackground(LightPink);
        detailPanel.setVisible(false);

        setupDetailComponents();

        // add sa main frame yung panels
        frame.add(headerPanel, BorderLayout.NORTH); //upper part
        frame.add(centerPanel, BorderLayout.CENTER); // periodic table & legend
        frame.add(detailPanel, BorderLayout.EAST); //element details
        
        frame.setVisible(true); //setting frame to visible to appear
    }

    private void setupGasButtons() {
        //element blocks and their position {symbol,x,y}
        String[][] gasData = { //array para di isa isa yung buttons
            {"H", "180", "100"}, {"He", "850", "100"}, {"N", "640", "190"},
            {"O", "710", "190"}, {"F", "780", "190"}, {"Ne", "850", "190"},
            {"Cl", "780", "280"}, {"Ar", "850", "280"}, {"Kr", "850", "370"},
            {"Xe", "850", "460"}, {"Rn", "850", "550"}
        };

        for (String[] data : gasData) {
            JButton btn = createGasButton(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2])); 
            //para matransfer to int yung String na xloc and yloc sa array kanina
            btnGas.add(btn);
            centerPanel.add(btn);
        }
    }

    //button image, element blocks graphics
    private JButton createGasButton(String symbol, int x, int y) {
        String name = getFileName(symbol);
        ImageIcon icon = new ImageIcon(getClass().getResource("/gaslabGraphics/" + name + ".png"));
        int normalWidth = 70;
        int normalHeight = 90;

        // para sumabay image sa size ng button
        Image scaled = icon.getImage().getScaledInstance(normalWidth, normalHeight, Image.SCALE_SMOOTH);
        JButton btn = new JButton(new ImageIcon(scaled));
        btn.setBounds(x, y, normalWidth, normalHeight);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        // para magiiba cursor pag tinapat sa element block
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover effect din para may indication na nakahover, magiibang kulay yung border ng element block pag naka hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            btn.setBorder(javax.swing.BorderFactory.createLineBorder(HeaderLineColor, 2));
            btn.setBorderPainted(true);
        }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            // hides the border when the mouse leaves
            btn.setBorderPainted(false);
        }
        });

        //para pag pinindot lilitaw yung element details
        btn.addActionListener(e -> {
            Gas g = logic.findGases(symbol);
            if (g != null) {
                showGasDetails(g);
                detailPanel.setVisible(true);
                frame.revalidate();
                frame.repaint();
            }
        });
        return btn;
    }

    //for element details and detail panel
    private void setupDetailComponents() {
        // image ng gas sa taas ng detail panel
        lblGasImage = new JLabel();
        lblGasImage.setBounds(25, 20, 300, 180);
        lblGasImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // name ng gas
        lblGasName = new JLabel("", SwingConstants.CENTER);
        lblGasName.setBounds(0, 210, 350, 30);
        lblGasName.setFont(new Font("Arial", Font.BOLD, 22));
        
        // type ng gas
        lblGasType = new JLabel("", SwingConstants.CENTER);
        lblGasType.setBounds(0, 240, 350, 20);

        // element properties
        propertyPanel = new JPanel(new BorderLayout());
        propertyPanel.setBounds(15, 280, 320, 160);
        propertyPanel.setOpaque(false);
        propertyPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)), "Properties",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), DarkBlue));

        // text details ng property panel
        txtDetails = new JTextArea();
        txtDetails.setEditable(false);
        txtDetails.setOpaque(false);
        txtDetails.setFont(new Font("Arial", Font.PLAIN, 18));
        propertyPanel.add(txtDetails);

        // element applications
        appPanel = new JPanel(new BorderLayout());
        appPanel.setBounds(15, 450, 320, 140);
        appPanel.setOpaque(false);
        appPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)), "Applications", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), DarkBlue));

        // text details ng application panel
        txtApp = new JTextArea();
        txtApp.setEditable(false);
        txtApp.setOpaque(false);
        txtApp.setFont(new Font("Arial", Font.PLAIN, 18));
        appPanel.add(txtApp);

        // back to full screen button
        btnBack = new JButton("◀ Back to Full Screen");
        btnBack.setBounds(75, 610, 200, 50);
        btnBack.setForeground(HeaderLineColor);
        btnBack.setBackground(new Color(147, 53, 61));
        btnBack.setContentAreaFilled(true);
        btnBack.setBorderPainted(true);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            detailPanel.setVisible(false);
            frame.revalidate();
            frame.repaint();
        });

        // add lahat ng attributes sa detail panel
        detailPanel.add(lblGasImage);
        detailPanel.add(lblGasName);
        detailPanel.add(lblGasType);
        detailPanel.add(propertyPanel);
        detailPanel.add(appPanel);
        detailPanel.add(btnBack);
    }

    //legend for the colors of noble gas and nonmetal
    private void setupLegend() {
        legendPanel = new JPanel(null);
        legendPanel.setOpaque(false);
        legendPanel.setBounds(100, 500, 180, 100);
        legendPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(HeaderLineColor), "Legend",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.PLAIN, 12), HeaderLineColor));
            
        //for noble gas    
        nobleBox = new JLabel();
        nobleBox.setBackground(Pink);
        nobleBox.setOpaque(true);
        nobleBox.setBounds(15, 30, 15, 15);

        // for nonmetal
        nonmetalBox = new JLabel();
        nonmetalBox.setBackground(Orange);
        nonmetalBox.setOpaque(true);
        nonmetalBox.setBounds(15, 60, 15, 15);
        
        // for noble gas
        nobleTxt = new JLabel("Noble Gases");
        nobleTxt.setForeground(HeaderLineColor);
        nobleTxt.setBounds(40, 30, 120, 20);

        // for nonmetal
        nonmetalTxt = new JLabel("Nonmetal Gases");
        nonmetalTxt.setForeground(HeaderLineColor);
        nonmetalTxt.setBounds(40, 60, 120, 20);
        
        // add sa legend panel
        legendPanel.add(nobleBox);
        legendPanel.add(nonmetalBox);
        legendPanel.add(nobleTxt);
        legendPanel.add(nonmetalTxt);
        centerPanel.add(legendPanel);
    }

    // icacallout tong method para lumitaw yung detail panel
    void showGasDetails(Gas gas) {
        lblGasName.setText(gas.getName());
        lblGasType.setText(gas instanceof NobleGas ? "Noble Gas" : "Nonmetal Gas");
        
        txtDetails.setText(String.format("""
            \u2022 Atomic Number: %d
            \u2022 Atomic Mass: %.4f
            \u2022 Configuration: %s
            """, gas.getAtomicNumber(), gas.getAtomicMass(), gas.getElectronConfig()));

        txtApp.setText(gas.getRealWorldUse());
        
        try {
            String path = "/gaslabGraphics/" + gas.getName().toLowerCase() + "Pic.png";
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image img = icon.getImage().getScaledInstance(lblGasImage.getWidth(), lblGasImage.getHeight(), Image.SCALE_SMOOTH);
            lblGasImage.setIcon(new ImageIcon(img));
        } catch (Exception ex) { lblGasImage.setIcon(null); }
    }

    // eto para malaman ng compiler anong tinutukoy na element if symbol lang prinovide
    private String getFileName(String symbol) {
        return switch (symbol) {
            case "H" -> "hydrogen"; case "He" -> "helium"; case "N" -> "nitrogen";
            case "O" -> "oxygen"; case "F" -> "fluorine"; case "Ne" -> "neon";
            case "Cl" -> "chlorine"; case "Ar" -> "argon"; case "Kr" -> "krypton";
            case "Xe" -> "xenon"; case "Rn" -> "radon";
            default -> "";
        };
    }

    // method to search, refer to logic in TheGasLab.java
    private void performSearch() {
    String query = searchField.getText().trim();
    Gas foundGas = logic.findGases(query);
    for (JButton btn : btnGas) {
        // if nahanap lilitaw yung block
        if (foundGas != null) {
            String btnSymbol = getSymbolFromButton(btn);
            btn.setVisible(btnSymbol.equalsIgnoreCase(foundGas.getSymbol()));
        // if di nahanap mawawala yung blocks
        } else {
            btn.setVisible(query.isEmpty());
        }
    }
    centerPanel.repaint();
}

// para ma read yung symbols per element block since image ginamit for btnGas
private String getSymbolFromButton(JButton btn) {
    int index = btnGas.indexOf(btn);
    String[][] gasData = {
        {"H"}, {"He"}, {"N"}, {"O"}, {"F"}, {"Ne"}, {"Cl"}, {"Ar"}, {"Kr"}, {"Xe"}, {"Rn"}
    };
    return gasData[index][0];
}

// main class, tinawag yung TheGasLabGUI para lumitaw yung UI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TheGasLabGUI::new);
    }
}
