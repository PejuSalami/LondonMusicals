/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.musicaltickets;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.print.PrinterException;



import java.io.FileInputStream;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import java.util.Map;
import java.util.Random;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextArea;





public class Home extends javax.swing.JFrame {

    public Home() {
        // Initialize Firebase
        try {
            FileInputStream serviceAccount = new FileInputStream("src/config/londonmusicals-53305-firebase-adminsdk-kvao6-a20e3db2ad.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://londonmusicals-53305-default-rtdb.europe-west1.firebasedatabase.app")
                .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); 
        }

        initComponents();
        basketArea.setEditable(false); 
        basketArea.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(ticketCountSpinner);
        ticketCountSpinner.setEditor(editor);
        editor.getTextField().setEditable(false);

        
        musicalDropdown.insertItemAt(" ", 0);
        selectMusicalDrop.insertItemAt(" ", 0); 
        selectDateDrop.insertItemAt(" ", 0); 
        selectTimeDrop.insertItemAt(" ", 0); 

        musicalDropdown.setSelectedIndex(0);
        selectMusicalDrop.setSelectedIndex(0);
        selectDateDrop.setSelectedIndex(0);
        selectTimeDrop.setSelectedIndex(0);
        loadDataFromFirebase();
//        
//         jComboBox1.insertItemAt(" ", 0); 
//
//    // Set the empty string as the default selected item
//    jComboBox1.setSelectedIndex(0);
        
//                        String exampleText = 
//        "Title: Mamma Mia\nRuntime: 1h 35min\nVenue: The O2\nAge: 16+ Under 16 will not be admitted\n\n" +
//        "Title: Matilda\nRuntime: 2h 35min\nVenue: Cambridge Theatre\nAge: 5+ Under 4 will not be admitted\n\n" +
//        "Title: Romeo and Juliet\nRuntime: 3h 30min\nVenue: Apollo Theatre\nAge: 13+ Under 12 will not be admitted\n\n" +
//        "Title: Les Misérables\nRuntime: 2h 50min\nVenue: Queen's Theatre\nAge: 12+ Under 11 will not be admitted\n\n" +
//        "Title: The Phantom of the Opera\nRuntime: 2h 20min\nVenue: Her Majesty's Theatre\nAge: 10+ Under 9 will not be admitted\n\n" +
//        "Title: Hamilton\nRuntime: 2h 40min\nVenue: Victoria Palace Theatre\nAge: 14+ Under 13 will not be admitted";
//
//
//    // Set text to musicalList
//    whatsOnList.setText(exampleText); 


    }



// Class variables
Map<String, String> musicalDetailsMap = new HashMap<>();
Map<String, Map<String, Map<String, Number>>> musicalToSeatsMap = new HashMap<>();
Map<String, Map<String, Number>> musicalDatesMap = new HashMap<>();
Map<String, Number> musicalTimesMap = new HashMap<>();
Map<String, Map<String, Number>> musicalPriceMap = new HashMap<>();
private void loadDataFromFirebase() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("musicals");
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            System.out.println("START OF FUNCTION");
            musicalDetailsMap.clear(); // Clear existing details
            musicalToSeatsMap.clear();
            musicalDatesMap.clear();
            musicalTimesMap.clear();
           
            System.out.println("AFTER MAPS");
            musicalPriceMap.clear();
            
            musicalDropdown.removeAllItems(); // Clear existing items
            musicalDropdown.addItem(" "); // Add an initial empty item

            System.out.println("MAPS AHOY");
            selectMusicalDrop.removeAllItems();
            selectMusicalDrop.addItem(" "); 
            System.out.println("MUSICAL DROP");
            selectDateDrop.removeAllItems(); // Clear existing items
            selectDateDrop.addItem(" "); 
            System.out.println("DATE DROP");
            selectTimeDrop.removeAllItems(); // Clear existing items
            selectTimeDrop.addItem(" ");
            System.out.println("TIME DROP");
            musicalSeatsRemaining.setText("-");
            System.out.println("STRING BUIKDER");
            StringBuilder allMusicalsInfo = new StringBuilder(); // StringBuilder for all musicals
            System.out.println("BEFORE LOOP");
            for (DataSnapshot musicalSnapshot : dataSnapshot.getChildren()) {
                String title = musicalSnapshot.child("title").getValue(String.class);
                musicalDropdown.addItem(title); 
                 selectMusicalDrop.addItem(title); 

                // Append the general information about the musical to allMusicalsInfo
                String runtime = musicalSnapshot.child("runtime").getValue(String.class);
                String venue = musicalSnapshot.child("venue").getValue(String.class);
                String age = musicalSnapshot.child("age").getValue(String.class);
                System.out.println("Hello my world");
                Map<String, Number> prices = new HashMap<>();
                Integer adultPrice = musicalSnapshot.child("price_adult").getValue(Integer.class);
                Integer seniorPrice = musicalSnapshot.child("price_senior").getValue(Integer.class);
                Integer studentPrice = musicalSnapshot.child("price_student").getValue(Integer.class);
                System.out.println("The price: " + adultPrice);
                if (adultPrice != null) {
                    prices.put("adult", (Number)adultPrice);
                }
                 if (seniorPrice != null) {
                    prices.put("senior", (Number)seniorPrice);
                }
                  if (studentPrice != null) {
                    prices.put("student", (Number)studentPrice);
                }
                musicalPriceMap.put(title, prices);
                
                allMusicalsInfo.append("Title: ").append(title).append("\n")
                               .append("Runtime: ").append(runtime).append("\n")
                               .append("Venue: ").append(venue).append("\n")
                               .append("Age: ").append(age).append("\n\n");

                StringBuilder details = new StringBuilder(); // StringBuilder for specific musical

                // Iterate through the seats data
                DataSnapshot seatsSnapshot = musicalSnapshot.child("seats");
                ArrayList<String> musicalDates = new ArrayList<>();
                Map<String, Map<String, Number>> dateTimes = new HashMap<>();
                for (DataSnapshot dateSnapshot : seatsSnapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    musicalDates.add(date);
                    details.append(date).append("\n"); // Append the date
                    
                    Map<String, Number> timeSeats = new HashMap<>();
                    for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                        String time = timeSnapshot.getKey();
                        Integer availableSeats = timeSnapshot.getValue(Integer.class);
                        timeSeats.put(time, availableSeats);

                        // Append time and available seats in a pleasing format
                        details.append("    Time: ").append(time)
                               .append(" - Available Seats: ").append(availableSeats)
                               .append("\n");
                    }
                    details.append("\n");
                    dateTimes.put(date, timeSeats);
                }

                // Store the detailed info in the map
                musicalDetailsMap.put(title, details.toString());
                musicalToSeatsMap.put(title, dateTimes);
            }

            // Print the contents of allMusicalsInfo to the console for debugging
            System.out.println("All Musicals Info: " + allMusicalsInfo.toString());

            // Set the text to whatsOnList text area
            //whatsOnList.setText(allMusicalsInfo.toString());
            
                whatsOnList.setLayout(new GridLayout(0, 4)); // 4 columns
                   for (String musicalInfo : allMusicalsInfo.toString().split("\n\n")) {
                JLabel label = new JLabel("<html>" + musicalInfo.replace("\n", "<br>") + "</html>");
                    whatsOnList.add(label);
                 }

                   // frame.add(whatsOnList);
            
            //whatsOnList.add(allMusicalsInfo.toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("The read failed: " + databaseError.getCode());
        }
    });

    // Set up an item listener for the dropdown
    musicalDropdown.addItemListener(e -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String selectedMusical = (String) e.getItem();
             if (selectedMusical != null && !selectedMusical.isBlank()) {
            nowShowingList.setText(musicalDetailsMap.getOrDefault(selectedMusical, "No details available."));
        }
        }
    });
    
    selectMusicalDrop.addItemListener(e -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String selectedMusical = (String) e.getItem();
            if (selectedMusical != null && !selectedMusical.isBlank()) {
                updateDateDropDown(selectedMusical);
                Map<String, Number> prices = musicalPriceMap.get(selectedMusical);
                adultLabel.setText("" + prices.get("adult"));
                seniorLabel.setText("" + prices.get("senior"));
                studentLabel.setText("" + prices.get("student"));
            }
            
        }
    });
    
    selectDateDrop.addItemListener(e -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String selectedDate = (String) e.getItem();
            if (selectedDate != null && !selectedDate.isBlank()) {
            updateTimeDropDown(selectedDate);
        }
        }
    });
    
    selectTimeDrop.addItemListener(e -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String selectedTime = (String) e.getItem();
            if (selectedTime != null && !selectedTime.isBlank()) {
            updateRemainingSeats(selectedTime);
        }
        }
    });
}

private void updateDateDropDown(String musical) {
    selectDateDrop.removeAllItems();
    selectDateDrop.addItem(" "); // Adding initial empty selection

    Map<String, Map<String, Number>> dates = musicalToSeatsMap.get(musical);
    
    if (dates != null) {
        musicalDatesMap = dates;
        for (String date : dates.keySet()) {
            selectDateDrop.addItem(date);
        }
    }
}

private void updateTimeDropDown(String date) {
    selectTimeDrop.removeAllItems();
    selectTimeDrop.addItem(" ");
    Map<String, Number> times = musicalDatesMap.get(date);
    
    if (times != null) {
        musicalTimesMap = times;
        for (String time : times.keySet()) {
            selectTimeDrop.addItem(time);
        }
    }
}

private void updateRemainingSeats(String time) {
    musicalSeatsRemaining.setText("-");
    Number seats = musicalTimesMap.get(time);
    if (seats != null) {
        musicalSeatsRemaining.setText("" + seats);
        SpinnerModel numberLimit = new SpinnerNumberModel(1, 1, (int)seats, 1);
        ticketCountSpinner.setModel(numberLimit);
    }
}

int ticketTotal = 0;
double totalPrice = 0;
private String getTicketInfo() {
    String musical = selectMusicalDrop.getSelectedItem().toString();
    String date = selectDateDrop.getSelectedItem().toString();
    String time = selectTimeDrop.getSelectedItem().toString();
    String ticketType = getSelectedButtonText(buttonGroup);
    int quantity = (Integer) ticketCountSpinner.getValue();
    ticketTotal += quantity;
    double price = getPrice(ticketType.toLowerCase(), musical) * quantity;
    totalPrice += price;
    String seatNumbers = assignSeats(musical, quantity);

    return "Musical: " + musical + "\nDate: " + date + "\nTime: " + time +
           "\nTicket Type: " + ticketType + "\nQuantity: " + quantity +
           "\nTotal Price: £" + price + "\nSeat Numbers: " + seatNumbers + "\n\n";
}

private String getSelectedButtonText(ButtonGroup buttonGroup) {
    for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
        AbstractButton button = buttons.nextElement();
        if (button.isSelected()) {
            return button.getText();
        }
    }
    return null;
}

private double getPrice(String ticketType, String musical) {
    System.out.println("TICKET");
    System.out.println(ticketType);
    Map<String, Number> prices = musicalPriceMap.get(musical);
    if (prices != null && prices.containsKey(ticketType)) {
        return prices.get(ticketType).doubleValue();
    }
    return 0; // Return 0 if no price is found
}


private String assignSeats(String musical, int quantity) {
    String selectedDate = selectDateDrop.getSelectedItem().toString();
    String selectedTime = selectTimeDrop.getSelectedItem().toString();

    Map<String, Map<String, Number>> dates = musicalToSeatsMap.get(musical);
    if (dates != null) {
        Map<String, Number> times = dates.get(selectedDate);
        if (times != null && times.containsKey(selectedTime)) {
            int seatsAvailable = times.get(selectedTime).intValue();
            // Generate random seat numbers here
            StringBuilder seats = new StringBuilder();
            Random rand = new Random();
            for (int i = 0; i < quantity; i++) {
                int seat = rand.nextInt(seatsAvailable) + 1; 
                seats.append(seat);
                if (i < quantity - 1) seats.append(", ");
            }
            return seats.toString();
        }
    }
    return "Not available"; 
}




private void updateBasketArea(String ticketInfo) {
    basketArea.append(ticketInfo);
}

private void resetFields() {
    selectMusicalDrop.setSelectedIndex(0);
    selectDateDrop.setSelectedIndex(0);
    selectTimeDrop.setSelectedIndex(0);

   
    buttonGroup.clearSelection();
    studentLabel.setText("");
    seniorLabel.setText("");
    adultLabel.setText("");
    
    ticketCountSpinner.setValue(1);

   
    musicalSeatsRemaining.setText("-");
}


private boolean validateInputs() {
    if (selectMusicalDrop.getSelectedIndex() <= 0) {
        return false;
    }

    if (selectDateDrop.getSelectedIndex() <= 0) {
        return false;
    }

    if (selectTimeDrop.getSelectedIndex() <= 0) {
        return false;
    }

    if (!isButtonGroupSelected(buttonGroup)) {
        return false;
    }

    if ((Integer)ticketCountSpinner.getValue() <= 0) {
        return false;
    }
    return true; 
}

private boolean isButtonGroupSelected(ButtonGroup group) {
   
    for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
        AbstractButton button = buttons.nextElement();
        if (button.isSelected()) {
            return true;
        }
    }
    return false;
}





    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        musicalListPan = new javax.swing.JPanel();
        whatsOnList = new javax.swing.JPanel();
        scheduleListPanContainer = new javax.swing.JPanel();
        musicalDropdown = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        nowShowingList = new java.awt.TextArea();
        jLabel2 = new javax.swing.JLabel();
        bookTicket = new javax.swing.JPanel();
        selectMusicalDrop = new javax.swing.JComboBox<>();
        selectDateDrop = new javax.swing.JComboBox<>();
        selectTimeDrop = new javax.swing.JComboBox<>();
        selectMusicalLabel = new javax.swing.JLabel();
        selectDateLabel = new javax.swing.JLabel();
        selectTimeLabel = new javax.swing.JLabel();
        adultRadioButton = new javax.swing.JRadioButton();
        seniorRadioButton = new javax.swing.JRadioButton();
        studentRadioButton = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        add = new javax.swing.JButton();
        ticketCountSpinner = new javax.swing.JSpinner();
        musicalSeatsRemaining = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        adultLabel = new javax.swing.JLabel();
        seniorLabel = new javax.swing.JLabel();
        studentLabel = new javax.swing.JLabel();
        basket = new javax.swing.JPanel();
        print = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        basketArea = new javax.swing.JTextArea();
        list = new javax.swing.JButton();
        schedule = new javax.swing.JButton();
        tickets = new javax.swing.JButton();
        cart = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        whatsOnList.setLayout(new java.awt.GridLayout());

        javax.swing.GroupLayout musicalListPanLayout = new javax.swing.GroupLayout(musicalListPan);
        musicalListPan.setLayout(musicalListPanLayout);
        musicalListPanLayout.setHorizontalGroup(
            musicalListPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(musicalListPanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(whatsOnList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        musicalListPanLayout.setVerticalGroup(
            musicalListPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(musicalListPanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(whatsOnList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab1", musicalListPan);

        musicalDropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("--select musical--");

        nowShowingList.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        nowShowingList.setEditable(false);

        jLabel2.setText("Now Showing");

        javax.swing.GroupLayout scheduleListPanContainerLayout = new javax.swing.GroupLayout(scheduleListPanContainer);
        scheduleListPanContainer.setLayout(scheduleListPanContainerLayout);
        scheduleListPanContainerLayout.setHorizontalGroup(
            scheduleListPanContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(nowShowingList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(scheduleListPanContainerLayout.createSequentialGroup()
                .addGroup(scheduleListPanContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(scheduleListPanContainerLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLabel1))
                    .addGroup(scheduleListPanContainerLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(musicalDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(161, 161, 161)
                        .addComponent(jLabel2)))
                .addContainerGap(468, Short.MAX_VALUE))
        );
        scheduleListPanContainerLayout.setVerticalGroup(
            scheduleListPanContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleListPanContainerLayout.createSequentialGroup()
                .addContainerGap(106, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(scheduleListPanContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(scheduleListPanContainerLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(musicalDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(scheduleListPanContainerLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel2)))
                .addGap(2, 2, 2)
                .addComponent(nowShowingList, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab2", scheduleListPanContainer);

        selectMusicalDrop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        selectDateDrop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        selectTimeDrop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        selectMusicalLabel.setText("--select musical--");

        selectDateLabel.setText("--select date--");

        selectTimeLabel.setText("--select time--");

        buttonGroup.add(adultRadioButton);
        adultRadioButton.setText("Adult");

        buttonGroup.add(seniorRadioButton);
        seniorRadioButton.setText("Senior");
        seniorRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seniorRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(studentRadioButton);
        studentRadioButton.setText("Student");

        jLabel3.setText("--select type of ticket--");

        add.setBackground(new java.awt.Color(51, 204, 0));
        add.setForeground(new java.awt.Color(255, 255, 255));
        add.setText("Add to Basket");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        ticketCountSpinner.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        musicalSeatsRemaining.setText("0");

        jLabel5.setText("--seats remaining--");

        jLabel4.setText("quantity");

        adultLabel.setText("£");

        seniorLabel.setText("£");

        studentLabel.setText("£");

        javax.swing.GroupLayout bookTicketLayout = new javax.swing.GroupLayout(bookTicket);
        bookTicket.setLayout(bookTicketLayout);
        bookTicketLayout.setHorizontalGroup(
            bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookTicketLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(selectMusicalLabel)
                .addGap(233, 233, 233)
                .addComponent(selectDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(selectTimeLabel)
                .addGap(123, 123, 123))
            .addGroup(bookTicketLayout.createSequentialGroup()
                .addGroup(bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(bookTicketLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(selectMusicalDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectDateDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bookTicketLayout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(musicalSeatsRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(307, 307, 307)
                        .addComponent(adultRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adultLabel))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bookTicketLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(jLabel5)))
                .addGroup(bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bookTicketLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(bookTicketLayout.createSequentialGroup()
                                .addGroup(bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(bookTicketLayout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addGroup(bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(ticketCountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(bookTicketLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(seniorRadioButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(seniorLabel)))
                                .addGap(24, 24, 24)
                                .addComponent(studentRadioButton)
                                .addGap(18, 18, 18)
                                .addComponent(studentLabel))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bookTicketLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                        .addComponent(selectTimeDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65))))
            .addGroup(bookTicketLayout.createSequentialGroup()
                .addGap(390, 390, 390)
                .addComponent(add)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bookTicketLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {adultRadioButton, seniorRadioButton, studentRadioButton});

        bookTicketLayout.setVerticalGroup(
            bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookTicketLayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addGroup(bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectMusicalLabel)
                    .addComponent(selectDateLabel)
                    .addComponent(selectTimeLabel))
                .addGap(18, 18, 18)
                .addGroup(bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectMusicalDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectDateDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectTimeDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(101, 101, 101)
                .addGroup(bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(bookTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adultRadioButton)
                    .addComponent(seniorRadioButton)
                    .addComponent(studentRadioButton)
                    .addComponent(musicalSeatsRemaining)
                    .addComponent(adultLabel)
                    .addComponent(seniorLabel)
                    .addComponent(studentLabel))
                .addGap(70, 70, 70)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ticketCountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(add)
                .addGap(41, 41, 41))
        );

        jTabbedPane1.addTab("tab3", bookTicket);

        print.setText("Print Ticket");
        print.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });

        basketArea.setBackground(new java.awt.Color(204, 204, 204));
        basketArea.setColumns(20);
        basketArea.setRows(5);
        jScrollPane1.setViewportView(basketArea);

        javax.swing.GroupLayout basketLayout = new javax.swing.GroupLayout(basket);
        basket.setLayout(basketLayout);
        basketLayout.setHorizontalGroup(
            basketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basketLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(192, 192, 192))
        );
        basketLayout.setVerticalGroup(
            basketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basketLayout.createSequentialGroup()
                .addGap(272, 272, 272)
                .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, basketLayout.createSequentialGroup()
                .addContainerGap(117, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab4", basket);

        jPanel2.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -130, 950, 640));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, 80, 950, 500));
        jPanel2.getAccessibleContext().setAccessibleParent(this);

        list.setText("What's On");
        list.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listActionPerformed(evt);
            }
        });
        jPanel1.add(list, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 100, -1));

        schedule.setText("Show Schedule");
        schedule.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        schedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scheduleActionPerformed(evt);
            }
        });
        jPanel1.add(schedule, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 130, -1));

        tickets.setText("Book Tickets");
        tickets.setActionCommand("");
        tickets.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ticketsActionPerformed(evt);
            }
        });
        jPanel1.add(tickets, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 130, -1));

        cart.setText("Basket");
        cart.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cartActionPerformed(evt);
            }
        });
        jPanel1.add(cart, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 30, 100, -1));

        exit.setText("Exit");
        exit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jPanel1.add(exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 30, 90, -1));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 63, 880, 10));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cartActionPerformed
        jTabbedPane1.setSelectedIndex(3);        // TODO add your handling code here:
    }//GEN-LAST:event_cartActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here: tab changed to 1

    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        // TODO add your handling code here:
              if (!validateInputs()) {
        // Show a message or perform an action if validation fails
        JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return; // Prevent further execution
    }
         String ticketInfo = getTicketInfo();
    updateBasketArea(ticketInfo);
    resetFields();

    }//GEN-LAST:event_addActionPerformed

    private void ticketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ticketsActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_ticketsActionPerformed

    private void scheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scheduleActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_scheduleActionPerformed

    private void listActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_listActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
            // TODO add your handling code here:
             System.exit(0); 
    }//GEN-LAST:event_exitActionPerformed

    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed
        // TODO add your handling code here:
      
        try {
            boolean complete = basketArea.print();
            if (complete) {
            JOptionPane.showMessageDialog(null,"Done printing", "Information",JOptionPane.INFORMATION_MESSAGE);
            } else{
                 JOptionPane.showMessageDialog(null,"Printing!", "Printer",JOptionPane.ERROR_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(null, pe);
        }
    }//GEN-LAST:event_printActionPerformed

    private void seniorRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seniorRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_seniorRadioButtonActionPerformed

    
    
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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JLabel adultLabel;
    private javax.swing.JRadioButton adultRadioButton;
    private javax.swing.JPanel basket;
    private javax.swing.JTextArea basketArea;
    private javax.swing.JPanel bookTicket;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton cart;
    private javax.swing.JButton exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton list;
    private javax.swing.JComboBox<String> musicalDropdown;
    private javax.swing.JPanel musicalListPan;
    private javax.swing.JLabel musicalSeatsRemaining;
    private java.awt.TextArea nowShowingList;
    private javax.swing.JButton print;
    private javax.swing.JButton schedule;
    private javax.swing.JPanel scheduleListPanContainer;
    private javax.swing.JComboBox<String> selectDateDrop;
    private javax.swing.JLabel selectDateLabel;
    private javax.swing.JComboBox<String> selectMusicalDrop;
    private javax.swing.JLabel selectMusicalLabel;
    private javax.swing.JComboBox<String> selectTimeDrop;
    private javax.swing.JLabel selectTimeLabel;
    private javax.swing.JLabel seniorLabel;
    private javax.swing.JRadioButton seniorRadioButton;
    private javax.swing.JLabel studentLabel;
    private javax.swing.JRadioButton studentRadioButton;
    private javax.swing.JSpinner ticketCountSpinner;
    private javax.swing.JButton tickets;
    private javax.swing.JPanel whatsOnList;
    // End of variables declaration//GEN-END:variables
}
