/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;
import com.bruynhuis.galago.filters.FXAAFilter;
import com.bruynhuis.galago.games.basic.BasicGameListener;
import com.bruynhuis.galago.screen.AbstractScreen;
import com.bruynhuis.galago.ui.Label;
import com.bruynhuis.galago.ui.TextAlign;
import com.bruynhuis.galago.ui.listener.TouchButtonAdapter;
import com.bruynhuis.galago.util.Debug;
import com.bruynhuis.galago.util.Timer;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import za.co.bruynhuis.ld41.MainApplication;
import za.co.bruynhuis.ld41.game.Game;
import za.co.bruynhuis.ld41.game.Opponent;
import za.co.bruynhuis.ld41.game.Pack;
import za.co.bruynhuis.ld41.game.Player;
import za.co.bruynhuis.ld41.ui.DeckPanel;
import za.co.bruynhuis.ld41.ui.GameOverDialog;
import za.co.bruynhuis.ld41.ui.HelpDialog;
import za.co.bruynhuis.ld41.ui.MessagePanel;
import za.co.bruynhuis.ld41.ui.PlayerPanel;
import za.co.bruynhuis.ld41.ui.ShowCallback;
import za.co.bruynhuis.ld41.ui.TimeoutCallback;

/**
 *
 * @author NideBruyn
 */
public class PlayScreen extends AbstractScreen implements BasicGameListener {

    public static final String NAME = "PlayScreen";
//    private Label title;
    private MainApplication mainApplication;
    private Game game;
    private Player player;
    private Opponent opponent;
    private float camDistance = 30;
    private PlayerPanel playerPanel;
    private PlayerPanel opponentPanel;
    private DeckPanel deckPanel;
    private Pack cardPack;
    private MessagePanel messagePanel;
    private int draws = 0;
    private int playerWins = 0;
    private int opponentWins = 0;
    private Label statsLabel;
    private Timer tickTimer = new Timer(10);

    private HelpDialog helpDialog;
    private GameOverDialog gameOverDialog;

    private FilterPostProcessor fpp;

    @Override
    protected void init() {
        mainApplication = (MainApplication) baseApplication;

//        title = new Label(hudPanel, "Screen Title");
//        title.centerTop(0, 0);
        statsLabel = new Label(hudPanel, "STATS"
                + "\nPlayer Wins: 0"
                + "\nOpponent Wins: 0"
                + "\nDraws: 0", 16, 300, 120);
        statsLabel.setAlignment(TextAlign.LEFT);
        statsLabel.setWrapMode(LineWrapMode.Word);
        statsLabel.leftTop(0, 0);

        messagePanel = new MessagePanel(hudPanel);
//        messagePanel.centerAt(0, 150);

        playerPanel = new PlayerPanel(hudPanel, "Player", ColorRGBA.Blue, true);
        playerPanel.leftBottom(10, 10);
        playerPanel.addTimeoutCallback(new TimeoutCallback() {
            @Override
            public void done() {
                playerPanel.setStay(true);
                doOpponentMove();
            }
        });
        playerPanel.addDrawButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    dealCard(playerPanel, new ShowCallback() {
                        @Override
                        public void shown() {
                            playerPanel.stopCountDown();
                            doOpponentMove();

                        }
                    });

                }
            }

        });

        playerPanel.addStayButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    playerPanel.setStay(true);
                    playerPanel.stopCountDown();
                    doOpponentMove();

                }
            }

        });

        opponentPanel = new PlayerPanel(hudPanel, "Opponent", ColorRGBA.Red, false);
        opponentPanel.rightBottom(10, 10);

        deckPanel = new DeckPanel(hudPanel);
        deckPanel.centerBottom(0, 10);

        helpDialog = new HelpDialog(window);
        helpDialog.addStartButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    mainApplication.addGamesRetried();
                    helpDialog.hide();
                    messagePanel.show();
                    mainApplication.getSoundManager().playSound("start");

                }
            }

        });

        gameOverDialog = new GameOverDialog(window);
        gameOverDialog.addRetryButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    showScreen(PlayScreen.NAME);

                }
            }

        });
        gameOverDialog.addExitButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    showScreen(MenuScreen.NAME);

                }
            }

        });

    }

    @Override
    protected void load() {

        draws = 0;
        playerWins = 0;
        opponentWins = 0;
        updateStats();

        game = new Game(mainApplication, rootNode, 10);
        game.load();

        player = new Player(game);
        player.load();

        opponent = new Opponent(game);
        opponent.load();

        game.addGameListener(this);

        cardPack = new Pack();
        cardPack.shuffle();

        fpp = new FilterPostProcessor(assetManager);
        baseApplication.getViewPort().addProcessor(fpp);

        SSAOFilter ssaof = new SSAOFilter();
        ssaof.setIntensity(2.5f);
        ssaof.setSampleRadius(1.5f);
        fpp.addFilter(ssaof);

        FXAAFilter fXAAFilter = new FXAAFilter();
        fpp.addFilter(fXAAFilter);

        //Setup the camera
        camera.setLocation(new Vector3f(player.getPosition().x, camDistance, player.getPosition().z + 40f));
        camera.lookAt(new Vector3f(player.getPosition().x, 0, player.getPosition().z), Vector3f.UNIT_Y);

    }

    @Override
    protected void show() {
        setPreviousScreen(MenuScreen.NAME);
        playerPanel.reset();
        opponentPanel.reset();
        playerPanel.setEnabled(false);

        messagePanel.addMessage("Go!");
        messagePanel.addMessage("1");
        messagePanel.addMessage("2");
        messagePanel.addMessage("3");
        messagePanel.addShowCallback(new ShowCallback() {
            @Override
            public void shown() {
                game.start(player);
                tickTimer.start();
                mainApplication.getSoundManager().playMusic("racing");
                updateStats();
                dealGame();
            }
        });

        if (mainApplication.getGamesRetried() == 0) {
            helpDialog.show();
        } else {
            messagePanel.show();
            mainApplication.getSoundManager().playSound("start");
        }

    }

    private void dealGame() {
        playerPanel.reset();
        opponentPanel.reset();
        deckPanel.reset();
        updateStats();

        dealCard(playerPanel, new ShowCallback() {
            @Override
            public void shown() {
                dealCard(opponentPanel, new ShowCallback() {
                    @Override
                    public void shown() {
                        dealCard(playerPanel, new ShowCallback() {
                            @Override
                            public void shown() {
                                dealCard(opponentPanel, new ShowCallback() {
                                    @Override
                                    public void shown() {
                                        doPlayerMove();

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

    private void dealCard(final PlayerPanel pPanel, final ShowCallback callback) {
        if (isActive() && !game.isGameOver()) {
            mainApplication.getSoundManager().playSound("deal");
            Vector3f targetPos = pPanel.getNextSlotPosition().subtract(deckPanel.getPosition());
            deckPanel.drawCard(targetPos.x, targetPos.y, new TweenCallback() {
                @Override
                public void onEvent(int i, BaseTween<?> bt) {
                    deckPanel.reset();
                    pPanel.addCard(cardPack.drawCard());
                    callback.shown();
                }
            });
        }

    }

    private void doOpponentMove() {
        playerPanel.setEnabled(false);

        if (game.isGameOver()) {
            return;
        }

        if (!opponentPanel.isStay()) {
            int total = opponentPanel.getTotalLowerCardScore();
            if (total < 11) {
                dealCard(opponentPanel, new ShowCallback() {
                    @Override
                    public void shown() {
                        doPlayerMove();
                    }
                });
            } else {
                opponentPanel.setStay(true);
                doPlayerMove();
            }
        } else {
            doPlayerMove();
        }

    }

    private void doPlayerMove() {

        if (game.isGameOver()) {
            return;
        }

        if (playerPanel.isStay()) {
            playerPanel.setEnabled(false);
            doCardRoundOutcome();

        } else if (playerPanel.isBusted()) {
            doOpponentWon(true);

        } else {
            playerPanel.setEnabled(true);
            playerPanel.doCountDown();

        }
    }

    private void doCardRoundOutcome() {

        if (game.isGameOver()) {
            return;
        }

        opponentPanel.revealCards();

        if (playerPanel.isBusted() && opponentPanel.isBusted()) {
            doDraw();

        } else if (!playerPanel.isBusted() && opponentPanel.isBusted()) {
            doPlayerWon(true);

        } else if (playerPanel.isBusted() && !opponentPanel.isBusted()) {
            doOpponentWon(true);

        } else {
            log("Else");
            int totalPlayer = playerPanel.getTotalHigherCardScore();
            int totalOpponent = opponentPanel.getTotalHigherCardScore();

            if (totalPlayer > 21) {
                totalPlayer = playerPanel.getTotalLowerCardScore();
            }

            if (totalOpponent > 21) {
                totalOpponent = opponentPanel.getTotalLowerCardScore();
            }

            log("Total Player: " + totalPlayer);
            log("Total Opponent: " + totalOpponent);

            if (totalPlayer > totalOpponent) {
                doPlayerWon(false);

            } else if (totalPlayer < totalOpponent) {
                doOpponentWon(false);

            } else {
                doDraw();
            }

        }

    }

    private void doPlayerWon(boolean busted) {
        playerWins++;
        updateStats();

        if (playerPanel.isDeck21()) {
            messagePanel.addMessage("Super Booster");

        } else if (playerPanel.isDeckFull()) {
            messagePanel.addMessage("Booster");

        } else {
            messagePanel.addMessage(" ");
        }

        messagePanel.addMessage("Player won");
        if (busted) {
            messagePanel.addMessage("Busted");
        }
        messagePanel.addShowCallback(new ShowCallback() {
            @Override
            public void shown() {
                player.increaseSpeed();
                if (playerPanel.isDeck21()) {
                    player.increaseSpeed();
                    player.increaseSpeed();

                } else if (playerPanel.isDeckFull()) {
                    player.increaseSpeed();

                }

                opponent.decreaseSpeed();
                dealGame();
            }
        });
        messagePanel.show();
    }

    private void doOpponentWon(boolean busted) {
        opponentWins++;
        updateStats();

        if (opponentPanel.isDeck21()) {
            messagePanel.addMessage("Super Booster");

        } else if (opponentPanel.isDeckFull()) {
            messagePanel.addMessage("Booster");

        } else {
            messagePanel.addMessage(" ");
        }

        messagePanel.addMessage("Dealer won");
        if (busted) {
            messagePanel.addMessage("Busted");
        }
        messagePanel.addShowCallback(new ShowCallback() {
            @Override
            public void shown() {
                player.decreaseSpeed();
                opponent.increaseSpeed();
                if (opponentPanel.isDeck21()) {
                    opponent.increaseSpeed();
                    opponent.increaseSpeed();

                } else if (opponentPanel.isDeckFull()) {
                    opponent.increaseSpeed();

                }
                dealGame();
            }
        });
        messagePanel.show();
    }

    private void doDraw() {
        draws++;
        updateStats();

        messagePanel.addMessage(" ");
        messagePanel.addMessage("Draw");
        messagePanel.addShowCallback(new ShowCallback() {
            @Override
            public void shown() {
                dealGame();
            }
        });
        messagePanel.show();

    }

    private void updateStats() {
        statsLabel.setText("STATS"
                + "\nPlayer Wins: " + playerWins
                + "\nOpponent Wins: " + opponentWins
                + "\nDraws: " + draws);

        if (player != null && opponent != null) {
            playerPanel.setStats(player.getLapCount(), player.getSpeed());
            opponentPanel.setStats(opponent.getLapCount(), opponent.getSpeed());
        } else {
            playerPanel.setStats(0, 0);
            opponentPanel.setStats(0, 0);
        }

    }

    @Override
    protected void exit() {
        mainApplication.getSoundManager().stopMusic("racing");
        mainApplication.getSoundManager().stopSound("start");
        player.setGameEnded();
        opponent.setGameEnded();

        playerPanel.reset();
        opponentPanel.reset();
        deckPanel.reset();
        game.close();
        baseApplication.getViewPort().removeProcessor(fpp);
    }

    @Override
    protected void pause() {
    }

    @Override
    public void doGameOver() {
//        throw new UnsupportedOpersationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doGameCompleted() {

        player.setGameEnded();
        opponent.setGameEnded();

        playerPanel.reset();
        opponentPanel.reset();
        deckPanel.reset();
        mainApplication.getSoundManager().stopMusic("racing");
        mainApplication.getSoundManager().playSound("endrace");
        mainApplication.getSoundManager().playSound("gameover");

        if (game.getWinner().equals(player)) {
            Debug.log("Player wins");
            gameOverDialog.setText("Well done you beat the Opponent. \n\nWould you like to play another game?");
            gameOverDialog.show();

        } else if (game.getWinner().equals(opponent)) {
            Debug.log("Opponent wins");
            gameOverDialog.setText("Oh no, the Opponent just won. \n\nWould you like to retry?");
            gameOverDialog.show();
        }

    }

    @Override
    public void doScoreChanged(int score) {
        updateStats();
    }

    @Override
    public void update(float tpf) {
        if (isActive()) {
            if (game.isStarted() && !game.isPaused()) {
                tickTimer.update(tpf);
                if (tickTimer.finished()) {
                    updateStats();
                    tickTimer.reset();
                }

                camera.setLocation(new Vector3f(player.getPosition().x, camDistance, player.getPosition().z + 40f));
                camera.lookAt(new Vector3f(player.getPosition().x, 0, player.getPosition().z), Vector3f.UNIT_Y);
            }
        }
    }

}
