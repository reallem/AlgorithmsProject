package falgorithmsproject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import falgorithmsproject.LocationData;  // Import the centralized data class

public class AmbulanceGuideUI2 extends JFrame {
    private JButton btnCalculate;
    private JTextField txtLocation;
    private JTextArea txtResult;
    private JScrollPane scrollPane;
    private JCheckBox chkTraffic;
    private JLabel lblEnterLocation;

    public AmbulanceGuideUI2() {
        setTitle("Ambulance Route Guide");
        setSize(500, 400);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        lblEnterLocation = new JLabel("Enter Current Location:");
        lblEnterLocation.setBounds(20, 20, 200, 30);
        add(lblEnterLocation);

        txtLocation = new JTextField();
        txtLocation.setBounds(220, 20, 250, 30);
        add(txtLocation);

        btnCalculate = new JButton("Find Nearest Hospital");
        btnCalculate.setBounds(150, 70, 200, 30);
        btnCalculate.addActionListener(this::performCalculation);
        add(btnCalculate);

        chkTraffic = new JCheckBox("Consider Traffic");
        chkTraffic.setBounds(150, 110, 200, 30);
        add(chkTraffic);

        txtResult = new JTextArea();
        txtResult.setEditable(false);
        scrollPane = new JScrollPane(txtResult);
        scrollPane.setBounds(20, 150, 450, 200);
        add(scrollPane);
    }

    private void performCalculation(ActionEvent e) {
        String inputLocation = txtLocation.getText().trim();
        int ambulanceLocation = -1;
        for (int i = 0; i < LocationData.NAMES.length; i++) {
            if (LocationData.NAMES[i].equalsIgnoreCase(inputLocation)) {
                ambulanceLocation = i;
                break;
            }
        }

        if (ambulanceLocation == -1) {
            txtResult.setText("Location not found: " + inputLocation);
            return;
        }

        double minDistance = Double.MAX_VALUE;
        int nearestHospital = -1;
        for (int hospitalIndex : LocationData.HOSPITAL_LOCATIONS) {
            double distance = haversine(LocationData.COORDINATES[ambulanceLocation][0], LocationData.COORDINATES[ambulanceLocation][1],
                                        LocationData.COORDINATES[hospitalIndex][0], LocationData.COORDINATES[hospitalIndex][1]);
            if (distance < minDistance) {
                minDistance = distance;
                nearestHospital = hospitalIndex;
            }
        }

        double speed = chkTraffic.isSelected() ? 30.0 : 120.0; // Speed in km/h
        double travelTime = minDistance / speed * 60; // Time in minutes
        txtResult.setText(String.format("Nearest hospital: %s\nDistance: %.2f km\nEstimated time: %.2f minutes",
                                        LocationData.NAMES[nearestHospital], minDistance, travelTime));
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371 * c; // Distance in kilometers
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AmbulanceGuideUI2().setVisible(true));
    }
}
