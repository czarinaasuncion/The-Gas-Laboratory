// imports
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class TheGasLabGUI extends JFrame {

    JFrame frame;
    JLabel lblGasImage, lblGasName, lblGasType, nobleBox, nonmetalBox, nobleTxt, nonmetalTxt;
    JPanel centerPanel, detailPanel, headerPanel, legendPanel, propertyPanel, appPanel;
    JButton btnBack;
    JTextArea txtDetails;
    List<JButton> btnGas;
    
    // mga colors na gagamitin
    private final Color DarkBlue = new Color(42, 70, 116);
    private final Color LightPink = new Color(244, 219, 221);
    private final Color HeaderLineColor = new Color(173, 216, 230); // Light Blue separator

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

        try {
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

        // center panel, yung mismong periodic table map
        centerPanel = new JPanel(null);
        centerPanel.setBackground(DarkBlue);
        btnGas = new ArrayList<>();

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
        //element blocks and their position
        String[][] gasData = {
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
        Image scaled = icon.getImage().getScaledInstance(70, 90, Image.SCALE_SMOOTH);
        
        JButton btn = new JButton(new ImageIcon(scaled));
        btn.setBounds(x, y, 70, 90);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //para pag pinindot lilitaw yung element details
        btn.addActionListener(e -> {
            Gas g = logic.findGases(symbol);
            if (g != null) {
                showGasDetails(g);
                detailPanel.setVisible(true);
                frame.revalidate(); // Forces layout recalculation for responsiveness
                frame.repaint();
            }
        });
        return btn;
    }

    //for element details and detail panel
    private void setupDetailComponents() {
        lblGasImage = new JLabel();
        lblGasImage.setBounds(25, 20, 300, 180);
        lblGasImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        lblGasName = new JLabel("", SwingConstants.CENTER);
        lblGasName.setBounds(0, 210, 350, 30);
        lblGasName.setFont(new Font("Arial", Font.BOLD, 22));
        
        lblGasType = new JLabel("", SwingConstants.CENTER);
        lblGasType.setBounds(0, 240, 350, 20);

        propertyPanel = new JPanel(new BorderLayout());
        propertyPanel.setBounds(15, 280, 320, 160);
        propertyPanel.setOpaque(false);
        propertyPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)), "Properties",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), DarkBlue));

        txtDetails = new JTextArea();
        txtDetails.setEditable(false);
        txtDetails.setOpaque(false);
        txtDetails.setFont(new Font("Arial", Font.PLAIN, 14));
        propertyPanel.add(txtDetails);

        appPanel = new JPanel(new BorderLayout());
        appPanel.setBounds(15, 450, 320, 140);
        appPanel.setOpaque(false);
        appPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)), "Applications", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), DarkBlue));

        btnBack = new JButton("<- Back to Full Screen");
        btnBack.setBounds(75, 610, 200, 50);
        btnBack.setForeground(new Color(147, 53, 61));
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            detailPanel.setVisible(false);
            frame.revalidate(); // Re-centers content when sidebar is hidden
            frame.repaint();
        });

        detailPanel.add(lblGasImage);
        detailPanel.add(lblGasName);
        detailPanel.add(lblGasType);
        detailPanel.add(propertyPanel);
        detailPanel.add(appPanel);
        detailPanel.add(btnBack);
    }

    //legend indicating the colors of noble gas and nonmetal
    private void setupLegend() {
        legendPanel = new JPanel(null);
        legendPanel.setOpaque(false);
        legendPanel.setBounds(60, 500, 180, 100);
        legendPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), "Legend",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.PLAIN, 12), Color.WHITE));
            
        nobleBox = new JLabel();
        nobleBox.setBackground(new Color(246, 154, 162));
        nobleBox.setOpaque(true);
        nobleBox.setBounds(15, 30, 15, 15);

        nonmetalBox = new JLabel();
        nonmetalBox.setBackground(new Color(252, 149, 79));
        nonmetalBox.setOpaque(true);
        nonmetalBox.setBounds(15, 60, 15, 15);
        
        nobleTxt = new JLabel("Noble Gases");
        nobleTxt.setForeground(Color.WHITE);
        nobleTxt.setBounds(40, 30, 120, 20);

        nonmetalTxt = new JLabel("Nonmetal Gases");
        nonmetalTxt.setForeground(Color.WHITE);
        nonmetalTxt.setBounds(40, 60, 120, 20);
        
        legendPanel.add(nobleBox);
        legendPanel.add(nonmetalBox);
        legendPanel.add(nobleTxt);
        legendPanel.add(nonmetalTxt);
        centerPanel.add(legendPanel);
    }

    void showGasDetails(Gas gas) {
        lblGasName.setText(gas.getName());
        lblGasType.setText(gas instanceof NobleGas ? "Noble Gas" : "Nonmetal Gas");
        
        txtDetails.setText(String.format("""
            \u2022 Atomic Number: %d
            \u2022 Atomic Mass: %.4f
            \u2022 Configuration: %s
            """, gas.getAtomicNumber(), gas.getAtomicMass(), gas.getElectronConfig()));

        appPanel.removeAll();
        JTextArea txtApp = new JTextArea(gas.getRealWorldUse());
        txtApp.setLineWrap(true);
        txtApp.setWrapStyleWord(true);
        txtApp.setOpaque(false);
        txtApp.setEditable(false);
        appPanel.add(txtApp);
        
        try {
            String path = "/gaslabGraphics/" + gas.getName().toLowerCase() + "Pic.png";
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image img = icon.getImage().getScaledInstance(lblGasImage.getWidth(), lblGasImage.getHeight(), Image.SCALE_SMOOTH);
            lblGasImage.setIcon(new ImageIcon(img));
        } catch (Exception ex) { lblGasImage.setIcon(null); }
    }

    private String getFileName(String symbol) {
        return switch (symbol) {
            case "H" -> "hydrogen"; case "He" -> "helium"; case "N" -> "nitrogen";
            case "O" -> "oxygen"; case "F" -> "fluorine"; case "Ne" -> "neon";
            case "Cl" -> "chlorine"; case "Ar" -> "argon"; case "Kr" -> "krypton";
            case "Xe" -> "xenon"; case "Rn" -> "radon";
            default -> "";
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TheGasLabGUI::new);
    }
}