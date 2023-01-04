import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class GameController implements MouseListener, Runnable, ChangeListener {
    //    private MapView mapView;
    private Setting setting;
    private GameView gameView;
    private Tutorial tutorial;

    private boolean isOpeningSetting = false;
    private int randomNum;
    private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = 1280, height = 720;
//        private int width = (int) dimension.getWidth(), height = (int) dimension.getHeight();
    private int mapPositionWidth[] = {height / 8 * 7 - height / 24, height / 8 * 6 - height / 12, height / 8 * 5 - height / 12, height / 8 * 4 - height / 12, height / 8 * 3 - height / 12, height / 8 * 1 - height / 24,
            height / 8 * 1 - height / 24, height / 8 * 1 - height / 24, height / 8 * 1 - height / 24, height / 8 * 1 - height / 24, height / 8 * 1 - height / 24, height / 8 * 3 - height / 12, height / 8 * 4 - height / 12,
            height / 8 * 5 - height / 12, height / 8 * 6 - height / 12, height / 8 * 7 - height / 24, height / 8 * 7 - height / 24, height / 8 * 7 - height / 24, height / 8 * 7 - height / 24, height / 8 * 7 - height / 24};
    ;
    private int mapPositionHeight[] = {height / 8 * 7 - height / 24, height / 8 * 7 - height / 24, height / 8 * 7 - height / 24, height / 8 * 7 - height / 24, height / 8 * 7 - height / 24, height / 8 * 7 - height / 24,
            height / 8 * 6 - height / 12 * 3 / 2, height / 8 * 5 - height / 12 * 3 / 2, height / 8 * 4 - height / 12 * 3 / 2, height / 8 * 3 - height / 12 * 3 / 2, height / 8 * 1 - height / 24, height / 8 * 1 - height / 24, height / 8 * 1 - height / 24,
            height / 8 * 1 - height / 24, height / 8 * 1 - height / 24, height / 8 * 1 - height / 24, height / 8 * 3 - height / 12 * 3 / 2, height / 8 * 4 - height / 12 * 3 / 2, height / 8 * 5 - height / 12 * 3 / 2, height / 8 * 6 - height / 12 * 3 / 2};
    private int currentPosition1 = 0, currentPosition2 = 0;
    private boolean isSetting = false, isPlayer1 = true, isTutorial = false, isOver = false;
    private int randomNumA, randomNumB, randomA, randomB;

    //<---------------------------------------------------------------------------Game Control---------------------------------------------------------------------->
    private Thread counter;
    private ArrayList<Thread> saveThread = new ArrayList<>();
    static int CountThread = 0;

    //หน้าจอ
    private EventView pokemonFightview, test;


    public int CheckFirstRound = 0;
    private int random = 0;
    private int warring = 0 , reply  = 0;

    // State
    private boolean boolCatching = false,
            boolviewcatch = false,
            boolBeforeFight = false;
    public int CountRound = 1, checkround = 0;
    private boolean stopfight = true;
    private boolean playerFight = false;


    private boolean state = true;

    // JoptinPlane
    private String[] response = {"Fight, Catch"};
    private int input;
    private JOptionPane MyJOptionPane;


    // Player
    private Player playerA;
    private Player playerB;
    private int FirstPerson = 0;
    private int CountRun = 0;

    private Pokemon enemyPokemon;

    // Rolling
    private int CountRoll = 0;

    // Test ระบบเกม
    int HP = 100;
    private ArrayList<EventView> eventView = new ArrayList<EventView>();
    
    // Sound
    private FloatControl fc;
    private int changeVolume = 100, valueSlider = 100;
    private float soundVolume = -30;
    private Clip menuClip, gameClip;
    private AudioInputStream audioStream;
    

    //<-------------------------------------------------------------------------------------------------------------------------------------------------------------->

    public GameController() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //<-------------------------------------Sound------------------------------------------------> 
        File file = new File("sound/menuSong.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        menuClip = AudioSystem.getClip();
        menuClip.open(audioStream);
        fc = (FloatControl)menuClip.getControl(FloatControl.Type.MASTER_GAIN);
        fc.setValue(soundVolume);
        menuClip.loop(Clip.LOOP_CONTINUOUSLY);

        menuClip.start();
        
        //<-------------------------------------Game Control------------------------------------------------>
        playerA = new Player(1);
        playerB = new Player(2);
        gameView = new GameView(width, height, playerA, playerB);
        gameView.getMenuView().getStartPanel().addMouseListener(this);
        gameView.getMenuView().getTutorialPanel().addMouseListener(this);
        gameView.getMenuView().getSettingPanel().addMouseListener(this);
        gameView.getMenuView().getExitPanel().addMouseListener(this);
    }

    public void run() {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.getSource().equals(gameView.getMenuView().getStartPanel())) {
            try{
                File file = new File("sound/menuClick.wav");
                audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
                menuClip.stop();
                
                File gameFile = new File("sound/gameSong.wav");
                AudioInputStream audioGameStream = AudioSystem.getAudioInputStream(gameFile);
                gameClip = AudioSystem.getClip();
                gameClip.open(audioGameStream);
                fc = (FloatControl)gameClip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                gameClip.loop(Clip.LOOP_CONTINUOUSLY);
                
                gameClip.start();
            }catch(Exception ex){}
            gameView.remove(gameView.getMenuView());
            gameView.revalidate();
            gameView.repaint();
//                state = STATE.GAME;
            gameView.getPlayer1().setPreferredSize(new Dimension(height / 24 * 3 / 2, height / 12 * 3 / 2));
            gameView.getPlayer1().setBackground(new Color(0, 0, 0, 0));
            gameView.getPlayer1().setBounds(height / 8 * 7 - height / 24, height / 8 * 7 - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
            gameView.add(gameView.getPlayer1());

            gameView.getPlayer2().setPreferredSize(new Dimension(height / 24 * 3 / 2, height / 12 * 3 / 2));
            gameView.getPlayer1().setBackground(new Color(0, 0, 0, 0));
            gameView.getPlayer2().setBounds(height / 8 * 7 - height / 24 + height / 48, height / 8 * 7 - height / 24 * 2, height / 24 * 3 / 2, height / 12 * 3 / 2);
            gameView.add(gameView.getPlayer2());

            gameView.add(gameView.getMapView());
            gameView.getMapView().setBounds(0, 0, width, height);
            gameView.revalidate();
            gameView.repaint();
            gameView.getMapView().getSettingPanel().addMouseListener(this);
            gameView.getMapView().getRollPanel().addMouseListener(this);
        } else if (e.getSource().equals(gameView.getMenuView().getExitPanel())) {
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            System.exit(0);
        } else if (e.getSource().equals(gameView.getMapView().getSettingPanel())) {
            try{
                File file = new File("sound/gameClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
                
                gameClip.stop();
                File menuFile = new File("sound/menuSong.wav");
                AudioInputStream audioMenuStream = AudioSystem.getAudioInputStream(menuFile);
                menuClip = AudioSystem.getClip();
                menuClip.open(audioMenuStream);
                fc = (FloatControl)menuClip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                menuClip.loop(Clip.LOOP_CONTINUOUSLY);
                
                menuClip.start();
            }catch(Exception ex){}
            
            isTutorial = false;
            currentPosition1 = 0;
            currentPosition2 = 0;
            boolCatching = false;
            boolviewcatch = false;
            boolBeforeFight = false;
            CountRound = 0;
            stopfight = true;
            playerFight = false;
            state = true;
            CheckFirstRound = 0;
            random = 0;
            warring = 0;
            CountThread = 0;
            FirstPerson = 0;
            CountRun = 0;
            CountRoll = 0;
            isPlayer1 = true;
            
            playerA = new Player(1);
            playerB = new Player(2);
            
            gameView.dispose();
            gameView = new GameView(width, height, playerA, playerB);
            gameView.getMenuView().getStartPanel().addMouseListener(this);
            gameView.getMenuView().getTutorialPanel().addMouseListener(this);
            gameView.getMenuView().getSettingPanel().addMouseListener(this);
            gameView.getMenuView().getExitPanel().addMouseListener(this);
        }
        else if(e.getSource().equals(gameView.getMenuView().getSettingPanel()) && isSetting == false) {
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            
            setting = new Setting(width, height, gameView, valueSlider);
            isSetting = true;
            setting.getFalseNotFullScreenPanel().addMouseListener(this);
            setting.getTrueNotFullScreenPanel().addMouseListener(this);
            setting.getFalseFullScreenPanel().addMouseListener(this);
            setting.getTrueFullScreenPanel().addMouseListener(this);
            setting.getSavePanel().addMouseListener(this);
            setting.getBackPanel().addMouseListener(this);
            setting.getVolume().addChangeListener(this);
        }else if(e.getSource().equals(gameView.getMenuView().getTutorialPanel()) && !isTutorial){
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            tutorial = new Tutorial(width, height, gameView);
            tutorial.getNext1().addMouseListener(this);
            tutorial.getNext2().addMouseListener(this);
            tutorial.getNext3().addMouseListener(this);
            tutorial.getNext4().addMouseListener(this);
            isTutorial = true;
        }else if(isTutorial && e.getSource().equals(tutorial.getNext1())){
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            tutorial.getAllPanel().remove(tutorial.getBackground1());
            tutorial.getAllPanel().add(tutorial.getBackground2(), BorderLayout.CENTER);
            tutorial.getWindow().revalidate();
            tutorial.getWindow().repaint();
        }else if(isTutorial && e.getSource().equals(tutorial.getNext2())){
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            tutorial.getAllPanel().remove(tutorial.getBackground2());
            tutorial.getAllPanel().add(tutorial.getBackground3(), BorderLayout.CENTER);
            tutorial.getWindow().revalidate();
            tutorial.getWindow().repaint();
        }else if(isTutorial && e.getSource().equals(tutorial.getNext3())){
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            tutorial.getAllPanel().remove(tutorial.getBackground3());
            tutorial.getAllPanel().add(tutorial.getBackground4(), BorderLayout.CENTER);
            tutorial.getWindow().revalidate();
            tutorial.getWindow().repaint();
        }else if(isTutorial && e.getSource().equals(tutorial.getNext4())){
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            tutorial.getWindow().dispose();
            isTutorial = false;
        }
        else if (e.getSource().equals(gameView.getMapView().getRollPanel())) {
            try{
                File file = new File("sound/gameClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            randomNum = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            this.random = randomNum;
            currentPosition1 %= 20;
            currentPosition2 %= 20;
            //<------------------------------------------Event Controll-------------------------------------------------------------------=>
            if(state == true) {
                if (isPlayer1 == true) {
                    gameView.getPlayer1().setBounds(mapPositionWidth[currentPosition1] - height / 48, mapPositionHeight[currentPosition1] - height / 48, height / 24 * 3 / 2, height / 12 * 3 / 2);
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                public void run() {
                                    //code run here
                                    go(playerA, false);
                                }
                            }, 10
                    );
                    isPlayer1 = !isPlayer1;
                    CountRun++;
                } else {
                    gameView.getPlayer2().setBounds(mapPositionWidth[currentPosition2] + height / 48, mapPositionHeight[currentPosition2] - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                public void run() {
                                    //code run here
                                    go(playerB, false);
                                }
                            }, 250
                    );
                    CountRun++;
                    isPlayer1 = !isPlayer1;
                }
                CountRoll += 1;
                state = false;
            }
        }
        else if(isSetting && e.getSource().equals(setting.getFalseFullScreenPanel())){
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            setting.getSetting().remove(setting.getFalseFullScreenPanel());
            setting.getSetting().remove(setting.getTrueNotFullScreenPanel());
            setting.getSetting().remove(setting.getSpace1());
            setting.getSetting().remove(setting.getSpace2());
            setting.getSetting().remove(setting.getSavePanel());
            setting.getSetting().remove(setting.getBackPanel());
            setting.getSetting().add(setting.getFalseNotFullScreenPanel());
            setting.getSetting().add(setting.getSpace1());
            setting.getSetting().add(setting.getTrueFullScreenPanel());
            setting.getSetting().add(setting.getSpace2());
            setting.getSetting().add(setting.getSavePanel());
            setting.getSetting().add(setting.getBackPanel());
            this.width = (int) dimension.getWidth();height = (int) dimension.getHeight();
            mapPositionWidth = new int[]{height/8*7-height/24, height/8*6-height/12,height/8*5-height/12,height/8*4-height/12,height/8*3-height/12,height/8*1-height/24,
                    height/8*1-height/24,height/8*1-height/24,height/8*1-height/24,height/8*1-height/24,height/8*1-height/24,height/8*3-height/12,height/8*4-height/12,
                    height/8*5-height/12,height/8*6-height/12,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24};
            mapPositionHeight = new int[]{height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,
                    height/8*6-height/12*3/2, height/8*5-height/12*3/2, height/8*4-height/12*3/2,height/8*3-height/12*3/2,height/8*1-height/24,height/8*1-height/24,height/8*1-height/24,
                    height/8*1-height/24,height/8*1-height/24,height/8*1-height/24,height/8*3-height/12*3/2,height/8*4-height/12*3/2,height/8*5-height/12*3/2,height/8*6-height/12*3/2};

            setting.getWindow().revalidate();
            setting.getWindow().repaint();
        }
        else if(isSetting && e.getSource().equals(setting.getFalseNotFullScreenPanel())){
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            setting.getSetting().remove(setting.getTrueFullScreenPanel());
            setting.getSetting().remove(setting.getFalseNotFullScreenPanel());
            setting.getSetting().remove(setting.getSpace1());
            setting.getSetting().remove(setting.getSpace2());
            setting.getSetting().remove(setting.getSavePanel());
            setting.getSetting().remove(setting.getBackPanel());
            setting.getSetting().add(setting.getTrueNotFullScreenPanel());
            setting.getSetting().add(setting.getSpace1());
            setting.getSetting().add(setting.getFalseFullScreenPanel());
            setting.getSetting().add(setting.getSpace2());
            setting.getSetting().add(setting.getSavePanel());
            setting.getSetting().add(setting.getBackPanel());
            this.width = 1280;height = 720;
            mapPositionWidth = new int[]{height/8*7-height/24, height/8*6-height/12,height/8*5-height/12,height/8*4-height/12,height/8*3-height/12,height/8*1-height/24,
                    height/8*1-height/24,height/8*1-height/24,height/8*1-height/24,height/8*1-height/24,height/8*1-height/24,height/8*3-height/12,height/8*4-height/12,
                    height/8*5-height/12,height/8*6-height/12,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24};
            mapPositionHeight = new int[]{height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,height/8*7-height/24,
                    height/8*6-height/12*3/2, height/8*5-height/12*3/2, height/8*4-height/12*3/2,height/8*3-height/12*3/2,height/8*1-height/24,height/8*1-height/24,height/8*1-height/24,
                    height/8*1-height/24,height/8*1-height/24,height/8*1-height/24,height/8*3-height/12*3/2,height/8*4-height/12*3/2,height/8*5-height/12*3/2,height/8*6-height/12*3/2};

            setting.getWindow().revalidate();
            setting.getWindow().repaint();
        }
        else if(isSetting && e.getSource().equals(setting.getSavePanel())){
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                fc = (FloatControl)menuClip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                fc = (FloatControl)gameClip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            valueSlider = (int) ((soundVolume+70)*2.5);
//            setting.getVolume().setValue();
            
            setting.getWindow().dispose();
            gameView.dispose();
            gameView = new GameView(width, height, playerA, playerB);
            gameView.getMenuView().getStartPanel().addMouseListener(this);
            gameView.getMenuView().getTutorialPanel().addMouseListener(this);
            gameView.getMenuView().getSettingPanel().addMouseListener(this);
            gameView.getMenuView().getExitPanel().addMouseListener(this);
            isSetting = false;
        }
        else if(isSetting && e.getSource().equals(setting.getBackPanel())){
            try{
                File file = new File("sound/menuClick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(soundVolume);
                clip.start();
            }catch(Exception ex){}
            setting.getWindow().dispose();
            isSetting = false;
        }

    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

//    public void actionPerformed(ActionEvent e) {
//        if(e.getSource().equals(setting.getOk())){
//            setting.getWindow().dispose();
//            isOpeningSetting = false;
//        }else if(e.getSource().equals(setting.getExit())){
//            System.exit(0);
//        }
//
//    }
    
    public void stateChanged(ChangeEvent e) {
        changeVolume = setting.getVolume().getValue();
        soundVolume = -70+changeVolume/5*2;
    }
    
    //<----------------------------------------Event Control-------------------------------------------------------->
    public void SHowPokemon(Player p) {

    }

    ///Main xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx


    // ActionListener ------------------------------------------------------------------------------------------------




    public void EndRound(int checkround) {
        if(!isPlayer1){
                playerA.getPokeball().add(playerA);
                gameView.getMapView().getPokeballLabel1().setText("x"+playerA.getPokeball().getAmount());
        }else{
            playerB.getPokeball().add(playerB);
            gameView.getMapView().getPokeballLabel2().setText("x"+playerB.getPokeball().getAmount());
        }
        if (checkround == 2) {
            CountRound += 1;
            gameView.getMapView().getRoundLabel().setText("Round "+CountRound);
            checkround = 0;
        }
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // Go ================================================================================================
    private void go(Player p, boolean catching) {

        //สั้งให้สร้าง Thread เอาไว้สำหรับ ขยับ animation เวลาที่เราเปลี่ยนหน้าไปมา
        counter = new Thread(new movingPokemon(p, catching));
        // เก็บ Thread ปัจจุบันไว้สำหรับ ทำง่าน และ kill เมื่อไม่ได้ใช้งาน
        saveThread.add(counter);
        // ั้งให้ Thread ตัวที่ 0 ทำงาน
        saveThread.get(saveThread.size() - 1).start();
        CountThread++;
    }

    

    //MOviewPOkemon =========================================================================================
    class movingPokemon implements Runnable {
        private Player player;
        private boolean catching;

        public movingPokemon(Player p, boolean catching) {
            player = p;
            boolCatching = catching;

        }

        public void run() {
            AddView(player, boolCatching);
        }
    }

    // Addview ****************************************************************************************************
    public void AddView(Player p, boolean catching) {
        ChangeEvent(p, catching);
    }

    // เปลี่ยนหน้าตัวละคร -----------------------------------------------------------------------------------------------
    public void ChangeEvent(Player p, boolean catching) {
        if(CountRound >=2){
            BorderLayout layout = (BorderLayout) gameView.getMapView().getMapPanel().getLayout();
            gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
            gameView.getMapView().getMapPanel().revalidate();
            gameView.getMapView().getMapPanel().repaint();
            gameView.getPlayer1().setBounds(mapPositionWidth[currentPosition1] - height / 48, mapPositionHeight[currentPosition1] - height / 48, height / 24 * 3 / 2, height / 12 * 3 / 2);
            gameView.getPlayer2().setBounds(mapPositionWidth[currentPosition2] + height / 48, mapPositionHeight[currentPosition2] - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
            gameView.getPlayer1().repaint();
        }else{
            if (catching == false) {
                //<----------------------------------------------Rolling----------------------------------->
                //dddd
                p.setCurrentPoint(p.getBeforePoint() + random);
                if (isPlayer1 == false) {
                    currentPosition1 += random;
                    currentPosition1 %= 20;
                } else {
                    currentPosition2 += random;
                    currentPosition2 %= 20;
                }
                if (p.getCurrentPoint() > 20) {
                    p.setCurrentPoint(p.getCurrentPoint() % 20);
                    p.setMyCountRound(1);
                    checkround += 1;
                    EndRound(checkround);
                }
                p.setBeforePoint(p.getCurrentPoint());
                //<----------------------------------------------Rolling----------------------------------->
                //ตาแรกนะครับ
                if (isPlayer1 == false) {

                    // ขยับ CheckRound
                    BorderLayout layout = (BorderLayout) gameView.getMapView().getMapPanel().getLayout();
                    gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                    gameView.getMapView().getMapPanel().revalidate();
                    gameView.getMapView().getMapPanel().repaint();
                    gameView.getPlayer1().setBounds(mapPositionWidth[currentPosition1] - height / 48, mapPositionHeight[currentPosition1] - height / 48, height / 24 * 3 / 2, height / 12 * 3 / 2);
                    gameView.getPlayer2().setBounds(mapPositionWidth[currentPosition2] + height / 48, mapPositionHeight[currentPosition2] - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
                    gameView.getPlayer1().repaint();
                    pokemonFightview = new EventView(-2, false, false, true, p,playerB, false, width, height,-300,-300,isPlayer1);
                    gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                    gameView.getMapView().getMapPanel().revalidate();
                    gameView.getMapView().getMapPanel().repaint();
                    gameView.getPlayer1().setBounds(mapPositionWidth[currentPosition1] - height / 48, mapPositionHeight[currentPosition1] - height / 48, height / 24 * 3 / 2, height / 12 * 3 / 2);
                    gameView.getPlayer2().setBounds(mapPositionWidth[currentPosition2] + height / 48, mapPositionHeight[currentPosition2] - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
                    gameView.getPlayer1().repaint();
                    try {
                        Thread.sleep(3000);
                    } catch (Exception i) {
                        i.printStackTrace();
                    }
                    try {
                        pokemonFightview = new EventView(p.getCurrentPoint(),
                                false,
                                false,
                                false,
                                p,
                                playerB,
                                false,
                                width,
                                height,
                                -300,
                                -300,
                                isPlayer1);
                        gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                        gameView.getMapView().getMapPanel().revalidate();
                        gameView.getMapView().getMapPanel().repaint();
                        gameView.getPlayer1().setBounds(mapPositionWidth[currentPosition1] - height / 48, mapPositionHeight[currentPosition1] - height / 48, height / 24 * 3 / 2, height / 12 * 3 / 2);
                        gameView.getPlayer2().setBounds(mapPositionWidth[currentPosition2] + height / 48, mapPositionHeight[currentPosition2] - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
                        gameView.getPlayer1().repaint();
                        gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                        gameView.getMapView().getMapPanel().revalidate();
                        gameView.getMapView().getMapPanel().repaint();
                        gameView.getPlayer1().setBounds(mapPositionWidth[currentPosition1] - height / 48, mapPositionHeight[currentPosition1] - height / 48, height / 24 * 3 / 2, height / 12 * 3 / 2);
                        gameView.getPlayer2().setBounds(mapPositionWidth[currentPosition2] + height / 48, mapPositionHeight[currentPosition2] - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
                        gameView.getPlayer1().repaint();
                        //Player A
                        if(p.getCurrentPoint() == 20){
                            playerA.getPokeball().add(playerA);
                            gameView.getMapView().getPokeballLabel1().setText("x"+playerA.getPokeball().getAmount());
                        }
                        if(p.getCurrentPoint() == 10){
                            p.getPotion().add(p);
                            gameView.getMapView().getPotionLabel1().setText("x"+p.getPotion().getAmount());
                        }
                        if (p.getCurrentPoint() == 5 || p.getCurrentPoint() == 10 || p.getCurrentPoint() == 15 || p.getCurrentPoint() == 20) {
                            state = true;
                            
                            gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getName2Panel1());
                            gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getSpace1());
                            gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getBagPanel1());
                            gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getName1Panel1());
                            gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getSpace1());
                            gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getBagPanel1());

                            gameView.getMapView().getPokeballLabel1().setText("x"+playerA.getPokeball().getAmount());
                            gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getName1Panel2());
                            gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getSpace2());
                            gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getBagPanel2());
                            gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getName2Panel2());
                            gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getSpace2());
                            gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getBagPanel2());
                            
                        }
                        if (gameView.getMapView().getMapPanel().isDisplayable() && (p.getCurrentPoint() != 5 && p.getCurrentPoint() != 10 && p.getCurrentPoint() != 15 && p.getCurrentPoint() != 20))
                        {
                            move(gameView, pokemonFightview, p);
                        }
                    } catch (Exception i) {
                        i.printStackTrace();
                    }

                }
                // ถ้าCatching ไม่เท่ากับ true หมายความว่า animation ที่กำลังจะสรา้งจะเป็น อนิเมชั่นต่อสู้ทันที่
                if (isPlayer1 == true) {

                    //<-------------------------Two----------------------------------------------------------->
                    // หยุดการทำงานของ Thread และ ลบ Thread ตัวนั้นออกจาก List
                    BorderLayout layout = (BorderLayout) gameView.getMapView().getMapPanel().getLayout();

                    gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                    gameView.getMapView().getMapPanel().revalidate();
                    gameView.getMapView().getMapPanel().repaint();
                    gameView.getPlayer1().setBounds(mapPositionWidth[currentPosition1] - height / 48, mapPositionHeight[currentPosition1] - height / 48, height / 24 * 3 / 2, height / 12 * 3 / 2);
                    gameView.getPlayer2().setBounds(mapPositionWidth[currentPosition2] + height / 48, mapPositionHeight[currentPosition2] - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
                    gameView.getPlayer1().repaint();
                    pokemonFightview = new EventView(-2, false, false, true, p,playerA, false, width, height,-300,-300,isPlayer1);
                    gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                    gameView.getMapView().getMapPanel().revalidate();
                    gameView.getMapView().getMapPanel().repaint();
                    gameView.getPlayer1().setBounds(mapPositionWidth[currentPosition1] - height / 48, mapPositionHeight[currentPosition1] - height / 48, height / 24 * 3 / 2, height / 12 * 3 / 2);
                    gameView.getPlayer2().setBounds(mapPositionWidth[currentPosition2] + height / 48, mapPositionHeight[currentPosition2] - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
                    gameView.getPlayer1().repaint();
                    try {
                        Thread.sleep(3000);
                    } catch (Exception i) {
                        i.printStackTrace();
                    }
                    try {
                        pokemonFightview = new EventView(p.getCurrentPoint(),
                                false,
                                false,
                                false,
                                playerB,
                                playerA,
                                false,
                                width,
                                height,
                                -300,
                                -300,
                                isPlayer1);

                        gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                        gameView.getMapView().getMapPanel().revalidate();
                        gameView.getMapView().getMapPanel().repaint();
                        gameView.getPlayer1().setBounds(mapPositionWidth[currentPosition1] - height / 48, mapPositionHeight[currentPosition1] - height / 48, height / 24 * 3 / 2, height / 12 * 3 / 2);
                        gameView.getPlayer2().setBounds(mapPositionWidth[currentPosition2] + height / 48, mapPositionHeight[currentPosition2] - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
                        gameView.getPlayer1().repaint();
                        gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                        gameView.getMapView().getMapPanel().revalidate();
                        gameView.getMapView().getMapPanel().repaint();
                        gameView.getPlayer1().setBounds(mapPositionWidth[currentPosition1] - height / 48, mapPositionHeight[currentPosition1] - height / 48, height / 24 * 3 / 2, height / 12 * 3 / 2);
                        gameView.getPlayer2().setBounds(mapPositionWidth[currentPosition2] + height / 48, mapPositionHeight[currentPosition2] - height / 24, height / 24 * 3 / 2, height / 12 * 3 / 2);
                        gameView.getPlayer1().repaint();
                        //Player B
                        if(p.getCurrentPoint() == 20){
                            playerB.getPokeball().add(playerB);
                            gameView.getMapView().getPokeballLabel2().setText("x"+playerB.getPokeball().getAmount());
                        }
                        if(p.getCurrentPoint() == 10){
                            p.getPotion().add(p);
                            gameView.getMapView().getPotionLabel2().setText("x"+p.getPotion().getAmount());
                        }
                        if (p.getCurrentPoint() == 5 || p.getCurrentPoint() == 10 || p.getCurrentPoint() == 15 || p.getCurrentPoint() == 20) {
                            state = true;
                            
                            gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getName2Panel2());
                            gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getSpace2());
                            gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getBagPanel2());
                            gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getName1Panel2());
                            gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getSpace2());
                            gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getBagPanel2());

                            gameView.getMapView().getPokeballLabel1().setText("x"+playerA.getPokeball().getAmount());
                            gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getName1Panel1());
                            gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getSpace1());
                            gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getBagPanel1());
                            gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getName2Panel1());
                            gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getSpace1());
                            gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getBagPanel1());
                            
                        }
                        if (gameView.getMapView().getMapPanel().isDisplayable() && (p.getCurrentPoint() != 5 && p.getCurrentPoint() != 10 && p.getCurrentPoint() != 15 && p.getCurrentPoint() != 20))
                        {
                            move(gameView, pokemonFightview, p);
                        }
                    } catch (Exception i) {
                        i.printStackTrace();
                    }

                }
                CheckFirstRound += 1;
                gameView.getPlayer1().repaint();

            }
            // สำหรับหน้าที่กำลังจับโปเกม่อน
            if (catching == true) {
                pokemonFightview = new EventView(-1,
                        false,
                        true,
                        false,
                        p,
                        playerA,
                        false,
                        width,
                        height,
                        -300,
                        -300,
                        isPlayer1);
                BorderLayout layout = (BorderLayout) gameView.getMapView().getMapPanel().getLayout();
                gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                gameView.getMapView().getMapPanel().revalidate();
                gameView.getMapView().getMapPanel().repaint();

            }
        }
    }


    //Player Fighting ==========================================================================================================================================================================
    public void playerFight(GameView gameView, EventView eventView, Player playerA, Player playerB) {
        try {
            // อยากจะให้เริ่มเดินเมื่อไหร่ ปรับตรงนี้ ถ้าตอนการให้เดินเลย ปรับให้เท่ากับข้างบน
            Thread.sleep(50);
            if (stopfight) {
                while (true) {
                    if (eventView.first) {
                        FightingA(gameView, eventView, playerA, playerB, randomA, randomB);
                    } else {
                        FightingB(gameView, eventView, playerA, playerB, randomA, randomB);
                    }
                }
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void FightingA(GameView gameView, EventView eventView, Player A, Player B, int randomA, int randomB){
        try {
            TimeUnit.MILLISECONDS.sleep(5);
            if (eventView.right) {
                eventView.xEnemyPokemon++;
                if (eventView.xEnemyPokemon == 100) {
                    if(A.getPokemon(randomA).getHP()>0) {
                        A.getPokemon(randomA).attack(B.getPokemon(randomB));
                    }
                    if(A.getPokemon(randomA).getHP()<= 20 && A.getPotion().getAmount() > 0){
                        A.getPotion().use(playerA);
                        A.getPokemon(randomA).setHP(20);
                        gameView.getMapView().getPotionLabel1().setText("x"+A.getPotion().getAmount());
                    }
                    if(A.getPokemon(randomA).getHP()<=0){
                        //แพ้
                        BorderLayout layout = (BorderLayout) gameView.getMapView().getMapPanel().getLayout();
                        gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                        gameView.getMapView().getMapPanel().revalidate();
                        gameView.getMapView().getMapPanel().repaint();
                        pokemonFightview = new EventView(500,
                                true,
                                false,
                                false,
                                playerA,
                                playerB,
                                false,
                                width,
                                height,
                                randomA,
                                randomB,
                                isPlayer1);
                        gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                        gameView.getMapView().getMapPanel().revalidate();
                        gameView.getMapView().getMapPanel().repaint();
                        Thread.sleep(5000);
                        if(!isOver){
                            try{
                                File file = new File("sound/gameClick.wav");
                                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                                Clip clip = AudioSystem.getClip();
                                clip.open(audioStream);
                                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                                fc.setValue(soundVolume);
                                clip.start();

                                gameClip.stop();
                                File menuFile = new File("sound/menuSong.wav");
                                AudioInputStream audioMenuStream = AudioSystem.getAudioInputStream(menuFile);
                                menuClip = AudioSystem.getClip();
                                menuClip.open(audioMenuStream);
                                fc = (FloatControl)menuClip.getControl(FloatControl.Type.MASTER_GAIN);
                                fc.setValue(soundVolume);
                                menuClip.loop(Clip.LOOP_CONTINUOUSLY);

                                menuClip.start();
                            }catch(Exception ex){}
                            gameView.dispose();
                            
                            isTutorial = false;
                            currentPosition1 = 0;
                            currentPosition2 = 0;
                            boolCatching = false;
                            boolviewcatch = false;
                            boolBeforeFight = false;
                            CountRound = 0;
                            stopfight = true;
                            playerFight = false;
                            state = true;
                            CheckFirstRound = 0;
                            random = 0;
                            warring = 0;
                            CountThread = 0;
                            FirstPerson = 0;
                            CountRun = 0;
                            CountRoll = 0;
                            isPlayer1 = true;

                            playerA = new Player(1);
                            playerB = new Player(2);

                            this.gameView = new GameView(width, height, playerA, playerB);
                            this.gameView.getMenuView().getStartPanel().addMouseListener(this);
                            this.gameView.getMenuView().getTutorialPanel().addMouseListener(this);
                            this.gameView.getMenuView().getSettingPanel().addMouseListener(this);
                            this.gameView.getMenuView().getExitPanel().addMouseListener(this);
                            isOver = true;
                        }
                    }
                    //Fighting
                    eventView.right = false;
                }
            }
            if (!eventView.right) {
                eventView.xEnemyPokemon--;
                if (eventView.xEnemyPokemon == 0) {
                    gameView.repaint();
                    pauseBlack();
                    eventView.right = true;
                    eventView.first = false;
                }
            }
            gameView.repaint();

        }
        catch (InterruptedException i) {
            Thread.currentThread().interrupt();
        }

    }
    public void FightingB(GameView gameView, EventView eventView, Player A, Player B, int randomA, int randomB){
        try {
            TimeUnit.MILLISECONDS.sleep(5);
            if (eventView.left) {
                eventView.xPlayerPokenom--;
                if (eventView.xPlayerPokenom == 150) {
                    System.out.println("=================================");
                    System.out.println("PokemonA : "+A.getPokemon(randomA).getHP());
                    System.out.println("PokemonB : "+B.getPokemon(randomB).getHP());
                    System.out.println("=================================");
                        //Fighting
                    if(B.getPokemon(randomB).getHP()>0) {
                        B.getPokemon(randomB).attack(A.getPokemon(randomA));
                    }
                    if(B.getPokemon(randomB).getHP()<= 20 && B.getPotion().getAmount() > 0){
                        B.getPotion().use(playerB);
                        B.getPokemon(randomB).setHP(20);
                        gameView.getMapView().getPotionLabel2().setText("x"+B.getPotion().getAmount());
                    }
                    if(B.getPokemon(0).getHP()<=0){
                        //แพ้
                        BorderLayout layout = (BorderLayout) gameView.getMapView().getMapPanel().getLayout();
                        gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                        gameView.getMapView().getMapPanel().revalidate();
                        gameView.getMapView().getMapPanel().repaint();
                        pokemonFightview = new EventView(600,
                                true,
                                false,
                                false,
                                playerA,
                                playerB,
                                false,
                                width,
                                height,
                                randomA,
                                randomB,
                                isPlayer1);
                        gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                        gameView.getMapView().getMapPanel().revalidate();
                        gameView.getMapView().getMapPanel().repaint();
                        Thread.sleep(5000);
                        if(!isOver){
                            try{
                                File file = new File("sound/gameClick.wav");
                                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                                Clip clip = AudioSystem.getClip();
                                clip.open(audioStream);
                                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                                fc.setValue(soundVolume);
                                clip.start();

                                gameClip.stop();
                                File menuFile = new File("sound/menuSong.wav");
                                AudioInputStream audioMenuStream = AudioSystem.getAudioInputStream(menuFile);
                                menuClip = AudioSystem.getClip();
                                menuClip.open(audioMenuStream);
                                fc = (FloatControl)menuClip.getControl(FloatControl.Type.MASTER_GAIN);
                                fc.setValue(soundVolume);
                                menuClip.loop(Clip.LOOP_CONTINUOUSLY);

                                menuClip.start();
                            }catch(Exception ex){}
                            gameView.dispose();

                            isTutorial = false;
                            currentPosition1 = 0;
                            currentPosition2 = 0;
                            boolCatching = false;
                            boolviewcatch = false;
                            boolBeforeFight = false;
                            CountRound = 0;
                            stopfight = true;
                            playerFight = false;
                            state = true;
                            CheckFirstRound = 0;
                            random = 0;
                            warring = 0;
                            CountThread = 0;
                            FirstPerson = 0;
                            CountRun = 0;
                            CountRoll = 0;
                            isPlayer1 = true;

                            playerA = new Player(1);
                            playerB = new Player(2);

                            this.gameView = new GameView(width, height, playerA, playerB);
                            this.gameView.getMenuView().getStartPanel().addMouseListener(this);
                            this.gameView.getMenuView().getTutorialPanel().addMouseListener(this);
                            this.gameView.getMenuView().getSettingPanel().addMouseListener(this);
                            this.gameView.getMenuView().getExitPanel().addMouseListener(this);
                            isOver = true;
                        }
                    }
                    eventView.left = false;
                }
            }
            if (!eventView.left) {
                eventView.xPlayerPokenom++;
                if (eventView.xPlayerPokenom == 200) {
                    gameView.repaint();
                    pauseBlack();
                    eventView.left = true;
                    eventView.first = true;
                }
            }
            gameView.repaint();
        } catch (Exception i) {
            Thread.currentThread().interrupt();
        }

    }



    // Move โปนเกม่อน>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public void move(JFrame gameView, EventView eventView, Player player) {
        try {
            boolviewcatch = false;
            warring = 0;
            // อยากจะให้เริ่มเดินเมื่อไหร่ ปรับตรงนี้ ถ้าตอนการให้เดินเลย ปรับให้เท่ากับข้างบน
            Thread.sleep(50);
            if (stopfight) {
                while (true) {
                    if (eventView.first) {
                        moveEnemy(gameView, eventView, player);
                    } else {
                        PlayerPokemon(gameView, eventView, player);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ชยับโปรเก่มอนศัตรู มุมบนขวา
    public   void moveEnemy(JFrame f ,EventView eventView, Player player){
        try {
            TimeUnit.MILLISECONDS.sleep(5);
            if (eventView.right) {
                eventView.xEnemyPokemon++;
                if (eventView.xEnemyPokemon == 100)
                {
                    if(eventView.getEnemyPokemon().getName() != null) {
                        if(player.getPokemon(0).getHP()>0) {
                            EnemyAttack(eventView.getEnemyPokemon(), player.getPokemon(0));
                        }
                    }
                    eventView.right = false;
                }
            }
            if (!eventView.right) {
                eventView.xEnemyPokemon--;
                if (eventView.xEnemyPokemon == 0) {
                    f.repaint();
                    pauseBlack();
                    eventView.right = true;
                    eventView.first = false;
                }
            }
            f.repaint();
        }
        catch (Exception i) {
            Thread.currentThread().interrupt();
        }

    }
    public   void PlayerPokemon(JFrame internalFrame , EventView eventView, Player player) {
        try {
            TimeUnit.MILLISECONDS.sleep(5);
            if (eventView.left) {
                eventView.xPlayerPokenom--;
                if (eventView.xPlayerPokenom == 150) {
                    if(eventView.getEnemyPokemon().getName() != null) {
                        if(eventView.getEnemyPokemon().getHP()>0) {
                            PlayerAttack(player.getPokemon(0), eventView.getEnemyPokemon());
                        }
                    }
                    eventView.left = false;
                }
            }
            if (!eventView.left) {
                eventView.xPlayerPokenom++;
                if (eventView.xPlayerPokenom == 200) {
                    internalFrame.repaint();
                    pauseBlack();
                    eventView.left = true;
                    eventView.first = true;
                }
            }
            internalFrame.repaint();
        } catch (InterruptedException i) {
            Thread.currentThread().interrupt();
        }

    }

    public void pauseBlack(){
        try {
            Thread.sleep((long) (1900));
        } catch(InterruptedException  e){
            Thread.currentThread().interrupt();
        }
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // ทำหน้าจับหรือปล่อย โปรเกม่อน
    public void EnemyAttack(Pokemon enemypokemon, Pokemon playerpokemon) {
        if(enemypokemon.getHP() > 30) {
            enemypokemon.attack(playerpokemon);
        }

    }
    public void PlayerAttack(Pokemon playerpokenon, Pokemon enmemypokemon) {
        BorderLayout layout = (BorderLayout) gameView.getMapView().getMapPanel().getLayout();
        playerpokenon.attack(enmemypokemon);
        if (enmemypokemon.getHP() < 30 && warring == 0 && ((!isPlayer1 && playerA.getPokeball().getAmount() > 0) || (isPlayer1 && playerB.getPokeball().getAmount() > 0))) {
            warring += 1;
            // ไม่เข้าใจแม่งเหมือนกัน แค่รู้ว่าใช้แล้ว Thread ตัวนั้นหยุดทำงาน
            String[] options = new String[]{"Catch", "Fight"};
            int response = JOptionPane.showOptionDialog(null, "Enemy Pokemon LOW!", "Catch Or Fight",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            if(response == 0) {
                if(CountRound >=2){
                    playerFight = true;
                }
                boolviewcatch = true;
                if (isPlayer1 == false && playerA.getPokeball().getAmount() > 0) {
                    playerA.getPokemons().add(enmemypokemon);
                    playerA.getPokeball().use(playerA);
                    gameView.getMapView().getPokeballLabel1().setText("x"+playerA.getPokeball().getAmount());
                    
                    gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getName2Panel1());
                    gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getSpace1());
                    gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getBagPanel1());
                    gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getName1Panel1());
                    gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getSpace1());
                    gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getBagPanel1());
                    
                    gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getName1Panel2());
                    gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getSpace2());
                    gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getBagPanel2());
                    gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getName2Panel2());
                    gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getSpace2());
                    gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getBagPanel2());
                    
//                    ("===================================");
//                    ("Player'A Pokemons");
//                    ("Pokemon1 : "+playerA.getPokemon(0));
//                    ("Pokemon2 : "+playerA.getPokemon(1));
//                    ("Pokemon3 : "+playerA.getPokemon(2));
//                    ("===================================");
                    if(playerA.getPokemons().size()==2){
                        JPanel pokemon2_1 = new ImagePanel(playerA.getPokemons().get(1).getPokemonImage(), height/12, height/12);
                        pokemon2_1.setPreferredSize(new Dimension(height/12, height/12));
                        pokemon2_1.setBackground(new Color(0,0,0,0));
                        gameView.getMapView().setPokemon2_1(pokemon2_1);
                        gameView.getMapView().getPokemon2Panel1().add(gameView.getMapView().getPokemon2_1());
                        gameView.getMapView().revalidate();
                        gameView.getMapView().repaint();
                    }else if(playerA.getPokemons().size()==3){
                        JPanel pokemon3_1 = new ImagePanel(playerA.getPokemons().get(2).getPokemonImage(), height/12, height/12);
                        pokemon3_1.setPreferredSize(new Dimension(height/12, height/12));
                        pokemon3_1.setBackground(new Color(0,0,0,0));
                        gameView.getMapView().setPokemon3_1(pokemon3_1);
                        gameView.getMapView().getPokemon3Panel1().add(gameView.getMapView().getPokemon3_1());
                        gameView.getMapView().revalidate();
                        gameView.getMapView().repaint();
                    }else{
                        playerA.removePokemon();
                        gameView.getMapView().getPokemon1Panel1().remove(gameView.getMapView().getPokemon1_1());
                        JPanel pokemon1_1 = new ImagePanel(playerA.getPokemons().get(0).getPokemonImage(), height/12, height/12);
                        pokemon1_1.setPreferredSize(new Dimension(height/12, height/12));
                        pokemon1_1.setBackground(new Color(0,0,0,0));
                        gameView.getMapView().setPokemon1_1(pokemon1_1);
                        gameView.getMapView().getPokemon1Panel1().add(pokemon1_1);
                        gameView.getMapView().getPokemon2Panel1().remove(gameView.getMapView().getPokemon2_1());
                        JPanel pokemon2_1 = new ImagePanel(playerA.getPokemons().get(1).getPokemonImage(), height/12, height/12);
                        pokemon2_1.setPreferredSize(new Dimension(height/12, height/12));
                        pokemon2_1.setBackground(new Color(0,0,0,0));
                        gameView.getMapView().setPokemon2_1(pokemon2_1);
                        gameView.getMapView().getPokemon2Panel1().add(pokemon2_1);
                        gameView.getMapView().getPokemon3Panel1().remove(gameView.getMapView().getPokemon3_1());
                        JPanel pokemon3_1 = new ImagePanel(playerA.getPokemons().get(2).getPokemonImage(), height/12, height/12);
                        pokemon3_1.setPreferredSize(new Dimension(height/12, height/12));
                        pokemon3_1.setBackground(new Color(0,0,0,0));
                        gameView.getMapView().setPokemon3_1(pokemon3_1);
                        gameView.getMapView().getPokemon3Panel1().add(pokemon3_1);

                        gameView.getMapView().getVariousMenuPanel().revalidate();
                        gameView.getMapView().getVariousMenuPanel().repaint();
                    }
                    gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                    pokemonFightview = new EventView(-1,
                            false,
                            true,
                            false,
                            playerA,
                            playerB,
                            false,
                            width,
                            height,
                            -300,
                            -300,
                            isPlayer1);
                    try{
                        Thread.sleep(5);
                        saveThread.remove(0);
                        saveThread.get(0).interrupt();
                    }catch(Exception e){
                        Thread.currentThread().interrupt(); // propagate interrupt
                    }

                    gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                    gameView.getMapView().getMapPanel().revalidate();
                    gameView.getMapView().getMapPanel().repaint();


                } else if(isPlayer1 && playerB.getPokeball().getAmount() > 0) {
                    playerB.getPokemons().add(enmemypokemon);
                    playerB.getPokeball().use(playerB);
                    gameView.getMapView().getPokeballLabel2().setText("x"+playerB.getPokeball().getAmount());
                    
                    gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getName2Panel2());
                    gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getSpace2());
                    gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getBagPanel2());
                    gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getName1Panel2());
                    gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getSpace2());
                    gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getBagPanel2());
                    
                    gameView.getMapView().getPokeballLabel1().setText("x"+playerA.getPokeball().getAmount());
                    gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getName1Panel1());
                    gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getSpace1());
                    gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getBagPanel1());
                    gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getName2Panel1());
                    gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getSpace1());
                    gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getBagPanel1());
                    
//                    ("===================================");
//                    ("Player'B Pokemons");
//                    ("Pokemon1 : "+playerB.getPokemon(0));
//                    ("Pokemon2 : "+playerB.getPokemon(1));
//                    ("Pokemon3 : "+playerB.getPokemon(2));
//                    ("===================================");
                    
                    if (playerB.getPokemons().size() == 2) {
                        JPanel pokemon2_2 = new ImagePanel(playerB.getPokemons().get(1).getPokemonImage(), height / 12, height / 12);
                        pokemon2_2.setPreferredSize(new Dimension(height / 12, height / 12));
                        pokemon2_2.setBackground(new Color(0, 0, 0, 0));
                        gameView.getMapView().setPokemon2_2(pokemon2_2);
                        gameView.getMapView().getPokemon2Panel2().add(gameView.getMapView().getPokemon2_2());
                        gameView.getMapView().revalidate();
                        gameView.getMapView().repaint();
                    } else if (playerB.getPokemons().size() == 3) {
                        JPanel pokemon3_2 = new ImagePanel(playerB.getPokemons().get(2).getPokemonImage(), height / 12, height / 12);
                        pokemon3_2.setPreferredSize(new Dimension(height / 12, height / 12));
                        pokemon3_2.setBackground(new Color(0, 0, 0, 0));
                        gameView.getMapView().setPokemon3_2(pokemon3_2);
                        gameView.getMapView().getPokemon3Panel2().add(gameView.getMapView().getPokemon3_2());
                        gameView.getMapView().revalidate();
                        gameView.getMapView().repaint();
                    } else {
                        playerB.removePokemon();

                        gameView.getMapView().getPokemon1Panel2().remove(gameView.getMapView().getPokemon1_2());
                        JPanel pokemon1_2 = new ImagePanel(playerB.getPokemons().get(0).getPokemonImage(), height / 12, height / 12);
                        pokemon1_2.setPreferredSize(new Dimension(height / 12, height / 12));
                        pokemon1_2.setBackground(new Color(0, 0, 0, 0));
                        gameView.getMapView().setPokemon1_2(pokemon1_2);
                        gameView.getMapView().getPokemon1Panel2().add(pokemon1_2);
                        gameView.getMapView().getPokemon2Panel2().remove(gameView.getMapView().getPokemon2_2());
                        JPanel pokemon2_2 = new ImagePanel(playerB.getPokemons().get(1).getPokemonImage(), height / 12, height / 12);
                        pokemon2_2.setPreferredSize(new Dimension(height / 12, height / 12));
                        pokemon2_2.setBackground(new Color(0, 0, 0, 0));
                        gameView.getMapView().setPokemon2_2(pokemon2_2);
                        gameView.getMapView().getPokemon2Panel2().add(pokemon2_2);
                        gameView.getMapView().getPokemon3Panel2().remove(gameView.getMapView().getPokemon3_2());
                        JPanel pokemon3_2 = new ImagePanel(playerB.getPokemons().get(2).getPokemonImage(), height / 12, height / 12);
                        pokemon3_2.setPreferredSize(new Dimension(height / 12, height / 12));
                        pokemon3_2.setBackground(new Color(0, 0, 0, 0));
                        gameView.getMapView().setPokemon3_2(pokemon3_2);
                        gameView.getMapView().getPokemon3Panel2().add(pokemon3_2);

                        gameView.getMapView().getVariousMenuPanel().revalidate();
                        gameView.getMapView().getVariousMenuPanel().repaint();
                    }
                    gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                    pokemonFightview = new EventView(-1,
                            false,
                            true,
                            false,
                            playerB,
                            playerA,
                            false,
                            width,
                            height,
                            -300,
                            -300,
                            isPlayer1);
                    try{
                        Thread.sleep(5);
                        saveThread.get(0).interrupt();
                        saveThread.remove(0);
                    }catch(Exception e){
                        Thread.currentThread().interrupt(); // propagate interrupt
                    }
                    gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                    gameView.getMapView().getMapPanel().revalidate();
                    gameView.getMapView().getMapPanel().repaint();
                    
                }
            }

        }
        if(enmemypokemon.getHP() <= 0 && boolviewcatch == false){
            playerFight = true;
            gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
            if(isPlayer1 == false){
                //เพิ่ม HP Pokemon ของ Player A
                gameView.getMapView().getPokeballLabel1().setText("x"+playerA.getPokeball().getAmount());
                gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getName2Panel1());
                gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getSpace1());
                gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getBagPanel1());
                gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getName1Panel1());
                gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getSpace1());
                gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getBagPanel1());

                gameView.getMapView().getPokeballLabel1().setText("x"+playerA.getPokeball().getAmount());
                gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getName1Panel2());
                gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getSpace2());
                gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getBagPanel2());
                gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getName2Panel2());
                gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getSpace2());
                gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getBagPanel2());
                
                for(int i = 0; i<playerA.getPokemons().size(); i++) {
                    playerA.getPokemon(i).setHP((int)(playerA.getPokemon(i).getHP()*0.1));
                    playerA.getPokemon(i).setATK((int)(playerA.getPokemon(i).getATK()*0.1));

                }
                // สร้างหน้าที่บอกว่าโปเกม่อนตาย
                pokemonFightview = new EventView(-3,
                        false,
                        false,
                        false,
                        playerA,
                        playerB,
                        true,
                        width,
                        height,
                        -300,
                        -300,
                        isPlayer1);
                gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                gameView.getMapView().getMapPanel().revalidate();
                gameView.getMapView().getMapPanel().repaint();
                try {
                    saveThread.get(0).interrupt();
                    saveThread.remove(0);
                } catch (Exception i) {
                    i.printStackTrace();
                }
                state = true;
            }
            else if(isPlayer1 == true){
                // เพิ่ม HP pokomon ของ Player B
                gameView.getMapView().getPokeballLabel1().setText("x"+playerA.getPokeball().getAmount());
                gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getName2Panel2());
                gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getSpace2());
                gameView.getMapView().getLeftVariousPanel2().remove(gameView.getMapView().getBagPanel2());
                gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getName1Panel2());
                gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getSpace2());
                gameView.getMapView().getLeftVariousPanel2().add(gameView.getMapView().getBagPanel2());

                gameView.getMapView().getPokeballLabel1().setText("x"+playerA.getPokeball().getAmount());
                gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getName1Panel1());
                gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getSpace1());
                gameView.getMapView().getLeftVariousPanel1().remove(gameView.getMapView().getBagPanel1());
                gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getName2Panel1());
                gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getSpace1());
                gameView.getMapView().getLeftVariousPanel1().add(gameView.getMapView().getBagPanel1());
                
                for(int i = 0; i<playerA.getPokemons().size(); i++) {
                    playerB.getPokemon(i).setHP((int)(playerB.getPokemon(i).getHP()*0.1));
                    playerB.getPokemon(i).setATK((int)(playerB.getPokemon(i).getATK()*0.1));
                }
                pokemonFightview = new EventView(-3,
                        false,
                        false,
                        false,
                        playerB,
                        playerA,
                        true,
                        width,
                        height,
                        -300,
                        -300,
                        isPlayer1);
                gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                gameView.getMapView().getMapPanel().revalidate();
                gameView.getMapView().getMapPanel().repaint();

                try {
                    saveThread.get(0).interrupt();
                    saveThread.remove(0);
                } catch (Exception i) {
                    i.printStackTrace();
                }
                state = true;
            }

        }

        if(CountRound  >= 2 && playerFight == true && reply == 0){
            for(int i = 0; i< playerA.getPokemons().size(); i++){
                randomNumA = i;
            }
            for(int i = 0; i< playerB.getPokemons().size(); i++){
                randomNumB = i;
            }
            randomA = ThreadLocalRandom.current().nextInt(0, randomNumA + 1);
            randomB = ThreadLocalRandom.current().nextInt(0, randomNumB + 1);
                
            playerA.getPokemon(randomA).setHP( 70-playerA.getPokemon(randomA).getHP());
            playerB.getPokemon(randomB).setHP( 70-playerB.getPokemon(randomB).getHP());
            
            reply +=1;
            gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
            pokemonFightview = new EventView(-200,
                    true,
                    false,
                    false,
                    playerB,
                    playerA,
                    true,
                    width,
                    height,
                    -300,
                    -300,
                    isPlayer1);
            gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
            gameView.getMapView().getMapPanel().revalidate();
            gameView.getMapView().getMapPanel().repaint();
            
            try{
                Thread.sleep(5000);
                gameView.getMapView().getMapPanel().remove(layout.getLayoutComponent(BorderLayout.CENTER));
                pokemonFightview = new EventView(-100,
                        true,
                        false,
                        false,
                        playerB,
                        playerA,
                        true,
                        width,
                        height,
                        randomA,
                        randomB,
                        isPlayer1);
                gameView.getMapView().getMapPanel().add(pokemonFightview, BorderLayout.CENTER);
                gameView.getMapView().getMapPanel().revalidate();
                gameView.getMapView().getMapPanel().repaint();
                if(pokemonFightview.isDisplayable()) {
                    playerFight(gameView, pokemonFightview, playerA, playerB);
                }

            }catch (Exception i){

                Thread.interrupted();
            }

            saveThread.remove(0);
        }
        state = true;

    }
    // JoptionPlane =======================================================================
    public  void GetJoptionPlane(){
        ImageIcon Icon= new ImageIcon("C:\\Users\\wallr\\Downloads\\pokeball-gif.gif");
        Image image = Icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        Icon = new ImageIcon(newimg);  // transform it back
        input = JOptionPane.showOptionDialog(gameView,
                "HP enemyPokemon are low!",
                "Choses!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.YES_NO_OPTION,
                Icon,
                response,
                0
        );

    }
}

