package ui;

import model.NationalPark;
import persistence.Load;
import persistence.Save;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

//This is the main UI class that handles all the images and JFrames, JPanels etc.
public class GamePanel {

    private final NationalPark nationalPark;
    private final JFrame frame;
    private final int buttonHeight = 30;
    private final int buttonWidth = 150;
    private final int offset = 32;
    private boolean isBuyPaneOpen = false;
    private boolean isPopulationPaneOpen = false;

    private JLabel cycleLabel;
    private JLabel moneyLabel;
    private JLabel imageLabel;
    private BuyingManager buyingManager;
    private PopulationManager populationManager;

    private String currentSelection;

    //header variables
    private final double headerScale = 0.08;
    private final Color headerColor = new Color(0,102,204);

    //body variables
    private final Color bodyColor = Color.GREEN;

    //animal selection variables
    private final double animalSelectionScale = 0.2;
    private final int animalSelectionHeight;
    private final Color animalSelectionColor = Color.RED;

    //buy window variables
    private final Color buyWindowColor = Color.GRAY;
    private final int fullBuyWindowWidth = 500;

    //the interactive panel window variables
    private final int imageSize = 256;
    private final Color numberPanelColor = Color.DARK_GRAY;

    //the populationWindowPane variables
    private final int populationWindowPaneWidth = 500;
    private final int populationWindowPaneHeight = 400;

    //the counterPanel variables
    private final Color counterPanelColor = Color.GRAY;

    //the popSelection variables
    private final double popSelectionScale = 0.2;
    private final Color popSelectionColor = Color.RED;

    //Begins to initialize the frame we are going to play on.
    public GamePanel() {

        final int scale = 75; //if 75 then 1200, 675 //if 50 then 800,450

        this.frame = new JFrame();
        this.nationalPark = new NationalPark();

        this.cycleLabel = new JLabel();
        this.moneyLabel = new JLabel();
        this.currentSelection = "rabbit";

        frame.setSize(16 * scale, 9 * scale + offset);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); //centres
        frame.setLayout(null); //makes it so we can use absolute positions
        frame.setResizable(false); //makes it so the user cant resize
        //frame.setUndecorated(true); //use this to abuse full screen

        this.animalSelectionHeight = (int) ((1 - headerScale) * frame.getHeight());

        addJPanelsToFrame();
        updateUI();

        //This just handles the closing of the
        frame.addWindowListener(new java.awt.event.WindowAdapter() {

            //EFFECTS: calls the static function in Printer, when the window is closed.
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                Printer.printLog();
            }
        });

        frame.setVisible(true);
    }


    //MODIFIES: this
    //EFFECTS: Adds all the necessary JPanels to the JFrame.
    private void addJPanelsToFrame() {

        //the populationWindowPane
        JLayeredPane populationWindowPane = createPopulationWindowPane();

        JPanel counterPanel = createCounterPanel((int) (getPopulationWindowPaneWidth() * (1 - getPopSelectionScale())),
                (int) (getPopulationWindowPaneWidth() * getPopSelectionScale()), getCounterPanelColor());
        populationWindowPane.add(counterPanel, JLayeredPane.PALETTE_LAYER);

        //the body pane
        JLayeredPane bodyLayerPane = createBodyLayerPane();

        //the buyWindow pane
        JLayeredPane buyWindowPane = createBuyWindowPane();

        List<JPanel> interactivePanels =
                createInteractivePanels((int) (getFullBuyWindowWidth() * (1 - getAnimalSelectionScale())),
                        getAnimalSelectionHeight(), getImageSize(),
                        (int) (getFullBuyWindowWidth() * getAnimalSelectionScale()), getNumberPanelColor());

        for (JPanel panel : interactivePanels) {
            buyWindowPane.add(panel, JLayeredPane.MODAL_LAYER);
        }

        getFrame().add(createHeader(getHeaderScale(), getHeaderColor(), buyWindowPane, populationWindowPane));

        setBuyingManager(new BuyingManager(interactivePanels.get(0), interactivePanels.get(1), getNationalPark()));
        setPopulationManager(new PopulationManager(counterPanel, getNationalPark()));

        setImageLabel(createImage());

        buyWindowPane.add(getImageLabel(), JLayeredPane.DRAG_LAYER);

        getFrame().add(bodyLayerPane);
        bodyLayerPane.add(buyWindowPane, JLayeredPane.PALETTE_LAYER);
        bodyLayerPane.add(populationWindowPane, JLayeredPane.PALETTE_LAYER);
    }

    //EFFECTS: creates a new image using the rabbit.png and defaults it to a particular location
    private JLabel createImage() {
        JLabel image = new JLabel(new ImageIcon("data/images/rabbit.png"));
        image.setBounds(0,0, getImageSize(), getImageSize());
        image.setLocation((int) ((getFullBuyWindowWidth() * getAnimalSelectionScale())
                        + ((getFullBuyWindowWidth() * (1 - getAnimalSelectionScale())) - getImageSize()) / 2),
                getImageSize() / 4);
        return image;
    }

    //EFFECTS: creates a JLayeredPane where all the body content should go
    private JLayeredPane createBodyLayerPane() {
        JLayeredPane bodyLayerPane = new JLayeredPane();

        bodyLayerPane.setBounds(0, (int) (getHeaderScale() * getFrame().getHeight()),
                getFrame().getWidth(), (int) ((1 - getHeaderScale()) * getFrame().getHeight()));

        bodyLayerPane.add(createBody(1 - getHeaderScale(), getBodyColor()),
                JLayeredPane.DEFAULT_LAYER);

        return bodyLayerPane;
    }

    //EFFECTS: creates the "drop-down" menu for the population button using JLayeredPane
    private JLayeredPane createPopulationWindowPane() {
        JLayeredPane populationWindowPane = new JLayeredPane();
        populationWindowPane.setBounds(0,0,getPopulationWindowPaneWidth(),getPopulationWindowPaneHeight());

        populationWindowPane.add(createPopSelection((int) (getPopulationWindowPaneWidth() * getPopSelectionScale()),
                getPopulationWindowPaneHeight(), getPopSelectionColor()), JLayeredPane.PALETTE_LAYER);

        populationWindowPane.setVisible(false);

        return populationWindowPane;

    }

    //EFFECTS: creates the "drop-down" menu for the buy button using JLayeredPane
    private JLayeredPane createBuyWindowPane() {
        JLayeredPane buyWindowPane = new JLayeredPane();
        buyWindowPane.setBounds(0,0, getFullBuyWindowWidth(), getAnimalSelectionHeight());

        buyWindowPane.add(createAnimalSelection((int) (getFullBuyWindowWidth() * getAnimalSelectionScale()),
                getAnimalSelectionHeight(), getAnimalSelectionColor()), JLayeredPane.PALETTE_LAYER);

        buyWindowPane.add(createBuyWindow((int) (getFullBuyWindowWidth() * (1 - getAnimalSelectionScale())),
                        getAnimalSelectionHeight(),
                        (int) (getFullBuyWindowWidth() * getAnimalSelectionScale()), getBuyWindowColor()),
                JLayeredPane.PALETTE_LAYER);

        buyWindowPane.setVisible(false);

        return buyWindowPane;
    }

    //REQUIRES: buyWindowPane to be the pane to be dropped down by the buy button.
    //  populationWindowPane to be the pane to be dropped down by the population button.
    //EFFECTS: creates the header for the game based on the given parameters.
    private JPanel createHeader(double scale, Color color,
                                JLayeredPane buyWindowPane,
                                JLayeredPane populationWindowPane) {
        JPanel header = createJPanel(0, 0,
                getFrame().getWidth(), (int) (getFrame().getHeight() * scale),
                color);
        header.setVisible(true);
        addHeaderButtons(header, buyWindowPane, populationWindowPane);
        return header;
    }

    //REQUIRES: buyWindowPane to be the pane to be dropped down by the buy button.
    //  populationWindowPane to be the pane to be dropped down by the population button.
    //MODIFIES: this
    //EFFECTS: adds all the buttons the header parameter.
    private void addHeaderButtons(JPanel header, JLayeredPane buyWindowPane, JLayeredPane populationWindowPane) {

        final int middleOfHeader = (header.getHeight() - getButtonHeight()) / 2;
        final int offset = 50;
        final int spacing = 50;
        int gap = spacing + offset;
        final List<String> buttonNames = List.of("Buy an animal!", "Population", "Next cycle", "Load", "Save");
        final List<ActionListener> actions = createHeaderButtonsFunctionality(buyWindowPane, populationWindowPane);

        JLabel money = createJLabel("000", 20,
                0, 0, 100, header.getHeight(), new Color(255,215,0));
        header.add(money);
        setMoneyLabel(money);

        createButtons(header, buttonNames.subList(0,3),
                gap, middleOfHeader,
                getButtonWidth(), getButtonHeight(),
                spacing, false, false, actions.subList(0,3));

        gap += 3 * (spacing + getButtonWidth());

        JLabel cycle = createJLabel("000", 20,
                gap - offset, 0, 100, header.getHeight(), Color.WHITE);
        header.add(cycle);
        setCycleLabel(cycle);

        gap = spacing + getButtonWidth();

        createButtons(header, buttonNames.subList(3,5), gap, middleOfHeader,
                getButtonWidth(), getButtonHeight(), spacing, false, true, actions.subList(3,5));

    }

    //REQUIRES: buyWindowPane to be the pane to be dropped down by the buy button.
    //  populationWindowPane to be the pane to be dropped down by the population button.
    //EFFECTS: creates the functionality of the buttons and returns the actions
    private List<ActionListener> createHeaderButtonsFunctionality(JLayeredPane buyWindowPane,
                                                                  JLayeredPane populationWindowPane) {
        List<ActionListener> actions = new ArrayList<>();
        actions.add(e -> toggleBuyWindow(buyWindowPane, populationWindowPane));
        actions.add(e -> togglePopulationWindow(populationWindowPane, buyWindowPane));
        actions.add(e -> nextCycle());
        actions.add(e -> {
            try {
                load(JOptionPane.showInputDialog("Name of the file you are trying to load?", null));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        actions.add(e -> {
            try {
                save(JOptionPane.showInputDialog("What do you wish to save the file as?", null));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        return actions;
    }

    //REQUIRES: a valid filename that exists in the data directory.
    //EFFECTS: loads the valid filename into the nationalPark
    private void load(String filename) throws FileNotFoundException {
        System.out.println(filename);
        Load load = new Load(filename);
        load.loadInto(getNationalPark());
        updateUI();
    }

    //REQUIRES: a valid filename
    //EFFECTS: save the state of the game into data/filename.json
    private void save(String filename) throws FileNotFoundException {
        Save save = new Save(filename);
        save.save(getNationalPark());
        updateUI();
    }

    //REQUIRES: buyWindowPane to be the pane to be dropped down by the buy button.
    //  populationWindowPane to be the pane to be dropped down by the population button.
    //MODIFIES: this
    //EFFECTS: closes the populationWindowPane and
    // toggles the state of the buyWindowPane (if it is closed, open it and vice versa)
    private void toggleBuyWindow(JLayeredPane buyWindowPane, JLayeredPane populationWindowPane) {
        if (getIsPopulationPaneOpen()) {
            togglePopulationWindow(populationWindowPane, buyWindowPane);
        }
        buyWindowPane.setVisible(!getIsBuyPaneOpen());
        setIsBuyPaneOpen(!getIsBuyPaneOpen());
        getBuyingManager().reset();
        setCurrentSelection("rabbit");
        updateUI();
    }

    //REQUIRES: populationWindowPane to be the pane to be dropped down by the population button.
    // buyWindowPane to be the pane to be dropped down by the buy button.
    //MODIFIES: this
    //EFFECTS: closes the buyWindowPane and
    // toggles the state of the populationWindowPane (if it is closed, open it and vice versa)
    private void togglePopulationWindow(JLayeredPane populationWindowPane, JLayeredPane buyWindowPane) {
        if (getIsBuyPaneOpen()) {
            toggleBuyWindow(buyWindowPane, populationWindowPane);
        }
        populationWindowPane.setVisible(!getIsPopulationPaneOpen());
        setIsPopulationPaneOpen(!getIsPopulationPaneOpen());
        getBuyingManager().reset();
        setCurrentSelection("rabbit");
        updateUI();
    }

    //MODIFIES: this
    //EFFECTS: go to the next cycle and update the ui
    private void nextCycle() {
        getNationalPark().nextCycle();
        updateUI();
    }

    //MODIFIES: this
    //EFFECTS: a function to call all UI updates
    private void updateUI() {
        updateCycle();
        updateMoney();
        updateImage();
        getPopulationManager().updateCounter(getCurrentSelection());
    }

    //MODIFIES: this
    //EFFECTS: a function to update the cycle value
    private void updateCycle() {
        getCycleLabel().setText(String.valueOf(getNationalPark().getCycle()));
    }

    //MODIFIES: this
    //EFFECTS: a function update the money value
    private void updateMoney() {
        getMoneyLabel().setText(String.valueOf(getNationalPark().getMoney()));
    }

    //MODIFIES: this
    //EFFECTS: a function to update
    private void updateImage() {
        getImageLabel().setIcon(new ImageIcon("data/images/" + currentSelection + ".png"));
    }

    //EFFECTS: simply creates the body of the JFrame
    private JPanel createBody(double scale, Color color) {
        JPanel body = createJPanel(0, 0,
                getFrame().getWidth(), (int) (getFrame().getHeight() * scale),
                color);
        body.setVisible(true);
        return body;
    }

    //EFFECTS: creates the animal selection panel of the buyWindowPane
    private JPanel createAnimalSelection(int width, int height, Color color) {
        JPanel animalSelection = createJPanel(0, 0,
                width, height,
                color);
        animalSelection.setVisible(true);
        addAnimalSelectionButtons(animalSelection);
        return animalSelection;
    }

    //EFFECTS: creates the buttons for the animalSelection JPanel
    private void addAnimalSelectionButtons(JPanel animalSelection) {

        final int middleOfAnimalSelection = (animalSelection.getWidth() - getButtonWidth()) / 2;
        final int spacing = 0;
        int gap = 0;
        List<String> allTypes = getNationalPark().getAllTypes();

        for (String animalName : allTypes) {
            Button button = new Button();
            button.setLabel(animalName);
            button.setSize(new Dimension(getButtonWidth(), getButtonHeight()));
            button.setBounds(middleOfAnimalSelection, gap,
                    getButtonWidth(), getButtonHeight());
            button.addActionListener(e -> changeCurrentSelection(animalName));
            animalSelection.add(button);
            gap += spacing + getButtonHeight();
        }
    }

    //EFFECTS: creates the buy window using the width and height
    private JPanel createBuyWindow(int width, int height, int spacing, Color color) {
        JPanel buyWindow = createJPanel(spacing, 0,
                width, height,
                color);
        buyWindow.setVisible(true);
        return buyWindow;
    }

    //EFFECTS: creates many JPanels that handles the user input in particular the buying of animals
    private List<JPanel> createInteractivePanels(int width, int height, int numberSize, int spacing, Color color) {
        JPanel numberPanel = createJPanel(spacing + (width - numberSize) / 2,
                (int) (numberSize * 1.5),
                numberSize, (int) (height - numberSize * 1.5) - getOffset(), color);

        numberPanel.setVisible(true);

        JLabel text = createJLabel("00", numberSize / 2,
                (numberPanel.getWidth() - numberSize) / 2,
                (numberPanel.getHeight() - numberSize) / 2,
                numberSize, numberSize, Color.WHITE);
        numberPanel.add(text);

        JPanel leftPanel = createLeftPanel((int) (numberSize * 1.5),
                (width - numberSize) / 2, (int) (height - numberSize * 1.5) - getOffset(),
                spacing, color);

        JPanel rightPanel = createRightPanel((int) (numberSize * 1.5),
                (width - numberSize) / 2, (int) (height - numberSize * 1.5) - getOffset(),
                spacing + numberSize + (width - numberSize) / 2, color);

        List<JPanel> interactivePanels = new ArrayList<>();
        interactivePanels.add(numberPanel);
        interactivePanels.add(leftPanel);
        interactivePanels.add(rightPanel);

        return interactivePanels;
    }

    //REQUIRES: this panel to be placed to the left of the central panel
    //EFFECTS: creates the left panel (left to the counter of the buyWindowPane)
    private JPanel createLeftPanel(int y, int width, int height, int spacing, Color color) {
        JPanel leftPanel = createJPanel(spacing,y,width,height,color);
        leftPanel.setVisible(true);
        addLeftPanelButtons(leftPanel);
        return leftPanel;
    }

    //EFFECTS: adds buttons the leftPanel
    private void addLeftPanelButtons(JPanel leftPanel) {
        final int buttonHeight = 30;
        final int buttonWidth = 70;
        final int topSpacing = 70;
        final int spacing = 20;
        final int middle = (leftPanel.getWidth() - buttonWidth) / 2;
        int gap = spacing;
        final List<String> buttonNames = List.of("+", "max");
        final List<ActionListener> actions = createLeftPanelButtonsFunctionality();

        createButtons(leftPanel, buttonNames, middle, topSpacing + gap,
                buttonWidth, buttonHeight, spacing, true, false, actions);

        gap += 2 * (spacing + buttonHeight);

        JLabel cost = createJLabel("00", (int) (sqrt(buttonHeight * buttonWidth) / 1.5),
                middle, topSpacing + gap, buttonWidth, buttonHeight, Color.RED);

        leftPanel.add(cost);
    }

    //EFFECTS: simply creates all the left panel button actions and returns that list
    private List<ActionListener> createLeftPanelButtonsFunctionality() {
        List<ActionListener> actions = new ArrayList<>();
        actions.add(e -> getBuyingManager().incrementCounter(getCurrentSelection(), false));
        actions.add(e -> getBuyingManager().incrementCounter(getCurrentSelection(), true));
        return actions;
    }

    //REQUIRES: this panel to be placed to the right of the central panel
    //EFFECTS: creates the right panel (right to the counter of the buyWindowPane)
    private JPanel createRightPanel(int y, int width, int height, int spacing, Color color) {
        JPanel rightPanel = createJPanel(spacing,y,width,height,color);
        rightPanel.setVisible(true);
        addRightPanelButtons(rightPanel);
        return rightPanel;
    }

    //EFFECTS: adds buttons to the right panel
    private void addRightPanelButtons(JPanel rightPanel) {
        final int buttonHeight = 30;
        final int buttonWidth = 70;
        final int topSpacing = 70;
        final int spacing = 20;
        final int middle = (rightPanel.getWidth() - buttonWidth) / 2;
        int gap = spacing;
        final List<String> buttonNames = List.of("-", "0", "Buy");
        final List<ActionListener> actions = createRightPanelButtonsFunctionality();

        int i = 0;
        for (String name : buttonNames) {
            Button button = new Button();
            button.setLabel(name);
            button.setSize(new Dimension(buttonWidth, buttonHeight));
            button.setBounds(middle,topSpacing + gap,
                    buttonWidth, buttonHeight);
            rightPanel.add(button);
            button.addActionListener(actions.get(i));
            gap += spacing + buttonHeight;
            i += 1;
        }

    }

    //EFFECTS: creates all the functionality of the right panel buttons
    private List<ActionListener> createRightPanelButtonsFunctionality() {
        List<ActionListener> actions = new ArrayList<>();
        actions.add(e -> getBuyingManager().decrementCounter(getCurrentSelection(), false));
        actions.add(e -> getBuyingManager().decrementCounter(getCurrentSelection(), true));
        actions.add(e -> buy());
        return actions;
    }

    //EFFECTS: creates the JPanel for the pop selection
    private JPanel createPopSelection(int width, int height, Color color) {
        JPanel popSelection = createJPanel(0, 0,
                width, height,
                color);
        popSelection.setVisible(true);
        addPopSelectionButtons(popSelection);
        return popSelection;
    }

    //EFFECTS: adds the buttons the pop selection panel and functionality
    private void addPopSelectionButtons(JPanel popSelection) {
        final int middleOfAnimalSelection = (popSelection.getWidth() - getButtonWidth()) / 2;
        final int spacing = 0;
        int gap = 0;
        List<String> allTypes = getNationalPark().getAllTypes();

        for (String animalName : allTypes) {
            Button button = new Button();
            button.setLabel(animalName);
            button.setSize(new Dimension(getButtonWidth(), getButtonHeight()));
            button.setBounds(middleOfAnimalSelection, gap,
                    getButtonWidth(), getButtonHeight());
            button.addActionListener(e -> getPopulationManager().updateCounter(animalName));
            popSelection.add(button);
            gap += spacing + getButtonHeight();
        }
    }

    //EFFECTS: buys the currently selected animal
    private void buy() {
        getBuyingManager().buy(getCurrentSelection());
        updateMoney();
        getBuyingManager().reset();
    }

    //REQUIRES: animalName to be in the list nationalPark.getAllTypes()
    //EFFECTS: changes the current selection to animalName
    private void changeCurrentSelection(String animalName) {
        setCurrentSelection(animalName);
        updateImage();
        getBuyingManager().reset();
    }

    //EFFECTS: creates the counter panel that keeps track of how many animals there are in the nationalPark
    private JPanel createCounterPanel(int size, int spacing, Color color) {
        JPanel counterPanel = createJPanel(spacing,0,size,size,color);
        counterPanel.setVisible(true);

        JLabel counter = new JLabel("00 (+0)");
        counter.setFont(new Font("Verdana", Font.BOLD, (int) sqrt(size)));
        counter.setHorizontalAlignment(SwingConstants.CENTER);
        counter.setVerticalAlignment(SwingConstants.CENTER);
        counter.setBounds(0,0,
                size, size);
        counter.setForeground(Color.WHITE);
        counter.setLocation(0, 0);
        counterPanel.add(counter);

        return counterPanel;
    }

    //EFFECTS: creates an abstract JPanel
    private JPanel createJPanel(int x, int y, int width, int height, Color color) {
        JPanel jpanel = new JPanel();
        jpanel.setVisible(false);
        jpanel.setBackground(color);
        jpanel.setBounds(x, y, width, height);
        jpanel.setLayout(null);
        return jpanel;
    }

    //EFFECTS: creates buttons adds them to the panel and then adds the functionality of each button from the
    // actions list.
    private void createButtons(JPanel panel, List<String> buttonNames,
                                 int x, int y,
                                 int buttonWidth, int buttonHeight,
                                 int spacing, boolean verticalLayout, boolean fromOtherSide,
                                 List<ActionListener> actions) {

        for (int i = 0; i < buttonNames.size(); i++) {
            Button button = new Button();
            String name = buttonNames.get(i);
            button.setLabel(name);
            button.setSize(buttonWidth, buttonHeight);
            if (!fromOtherSide) {
                button.setBounds(x, y, buttonWidth, buttonHeight);
            } else {
                button.setBounds(panel.getWidth() - x, y, buttonWidth, buttonHeight);
            }
            button.addActionListener(actions.get(i));
            panel.add(button);

            if (!verticalLayout) {
                x += spacing + buttonWidth;
            } else {
                y += spacing + buttonHeight;
            }
        }
    }

    //EFFECTS: Creates an abstract JLabel.
    private JLabel createJLabel(String initialText, int size, int x, int y,
                                int width, int height, Color color) {
        JLabel label = new JLabel(initialText);
        label.setFont(new Font("Verdana", Font.BOLD, size));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBounds(0,0, width, height);
        label.setForeground(color);
        label.setLocation(x,y);
        return label;
    }

    // --------------------- SETTERS AND GETTERS BELOW!

    //EFFECTS: simple getter of animalSelectionColor
    public Color getAnimalSelectionColor() {
        return animalSelectionColor;
    }

    //EFFECTS: simple getter of bodyColor
    public Color getBodyColor() {
        return bodyColor;
    }

    //EFFECTS: simple getter of buyWindowColor
    public Color getBuyWindowColor() {
        return buyWindowColor;
    }

    //EFFECTS: simple getter of counterPanelColor
    public Color getCounterPanelColor() {
        return counterPanelColor;
    }

    //EFFECTS: simple getter of headerColor
    public Color getHeaderColor() {
        return headerColor;
    }

    //EFFECTS: simple getter of numberPanelColor
    public Color getNumberPanelColor() {
        return numberPanelColor;
    }

    //EFFECTS: simple getter of popSelectionColor
    public Color getPopSelectionColor() {
        return popSelectionColor;
    }

    //EFFECTS: simple getter of animalSelectionScale
    public double getAnimalSelectionScale() {
        return animalSelectionScale;
    }

    //EFFECTS: simple getter of headerScale
    public double getHeaderScale() {
        return headerScale;
    }

    //EFFECTS: simple getter of popSelectionScale
    public double getPopSelectionScale() {
        return popSelectionScale;
    }

    //EFFECTS: simple getter of animalSelectionHeight
    public int getAnimalSelectionHeight() {
        return animalSelectionHeight;
    }

    //EFFECTS: simple getter of buttonHeight
    public int getButtonHeight() {
        return buttonHeight;
    }

    //EFFECTS: simple getter of buttonWidth
    public int getButtonWidth() {
        return buttonWidth;
    }

    //EFFECTS: simple getter of fullBuyWindowWidth
    public int getFullBuyWindowWidth() {
        return fullBuyWindowWidth;
    }

    //EFFECTS: simple getter of imageSize
    public int getImageSize() {
        return imageSize;
    }

    //EFFECTS: simple getter of offset
    public int getOffset() {
        return offset;
    }

    //EFFECTS: simple getter of populationWindowPaneHeight
    public int getPopulationWindowPaneHeight() {
        return populationWindowPaneHeight;
    }

    //EFFECTS: simple getter of populationWindowPaneWidth
    public int getPopulationWindowPaneWidth() {
        return populationWindowPaneWidth;
    }

    //EFFECTS: simple getter of currentSelection
    public String getCurrentSelection() {
        return currentSelection;
    }

    //EFFECTS: simple getter of buyingManager
    public BuyingManager getBuyingManager() {
        return buyingManager;
    }

    //EFFECTS: simple getter of frame
    public JFrame getFrame() {
        return frame;
    }

    //EFFECTS: simple getter of cycleLabel
    public JLabel getCycleLabel() {
        return cycleLabel;
    }

    //EFFECTS: simple getter of imageLabel
    public JLabel getImageLabel() {
        return imageLabel;
    }

    //EFFECTS: simple getter of moneyLabel
    public JLabel getMoneyLabel() {
        return moneyLabel;
    }

    //EFFECTS: simple getter of nationalPark
    public NationalPark getNationalPark() {
        return nationalPark;
    }

    //EFFECTS: simple getter of populationManager
    public PopulationManager getPopulationManager() {
        return populationManager;
    }

    //EFFECTS: simple getter of isPopulationPaneOpen
    public boolean getIsPopulationPaneOpen() {
        return isPopulationPaneOpen;
    }

    //EFFECTS: simple getter of isBuyPaneOpen
    public boolean getIsBuyPaneOpen() {
        return isBuyPaneOpen;
    }

    //REQUIRES: currentSelection must be in nationalPark.getAllTypes()
    //MODIFIES: this
    //EFFECTS: simple setter of animals
    public void setCurrentSelection(String currentSelection) {
        this.currentSelection = currentSelection;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of animals
    public void setIsBuyPaneOpen(boolean buyPaneOpen) {
        isBuyPaneOpen = buyPaneOpen;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of isPopulationPaneOpen
    public void setIsPopulationPaneOpen(boolean populationPaneOpen) {
        isPopulationPaneOpen = populationPaneOpen;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of cycleLabel
    public void setCycleLabel(JLabel cycleLabel) {
        this.cycleLabel = cycleLabel;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of imageLabel
    public void setImageLabel(JLabel imageLabel) {
        this.imageLabel = imageLabel;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of moneyLabel
    public void setMoneyLabel(JLabel moneyLabel) {
        this.moneyLabel = moneyLabel;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of buyingManager
    public void setBuyingManager(BuyingManager buyingManager) {
        this.buyingManager = buyingManager;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of populationManager
    public void setPopulationManager(PopulationManager populationManager) {
        this.populationManager = populationManager;
    }
}

