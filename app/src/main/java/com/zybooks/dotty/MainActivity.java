package com.zybooks.dotty;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private DotsGame mGame;
    private DotsView mDotsView;
    private TextView mMovesRemaining;
    private TextView mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMovesRemaining = findViewById(R.id.moves_remaining_text_view);
        mScore = findViewById(R.id.score_text_view);
        mDotsView = findViewById(R.id.dots_view);
        mDotsView.setGridListener(mGridListener);
        findViewById(R.id.new_game_button).setOnClickListener(this::newGameClick);
        mGame = DotsGame.getInstance();
        startNewGame();
    }

    private final DotsView.DotsGridListener mGridListener = new DotsView.DotsGridListener() {
        @Override
        public void onDotSelected(Dot dot, DotsView.DotSelectionStatus selectionStatus) {
// Ignore selections when game is over
            if (mGame.isGameOver()) return;
// Add/remove dot to/from selected dots
            DotsGame.DotStatus addStatus = mGame.processDot(dot);
// If done selecting dots then replace selected dots and display new moves and score
            if (selectionStatus == DotsView.DotSelectionStatus.Last) {
                if (mGame.getSelectedDots().size() > 1) {
                    mGame.finishMove();
                    updateMovesAndScore();
                } else {
                    mGame.clearSelectedDots();
                }
            }
// Display changes to the game
            mDotsView.invalidate();
        }
    };

    private void newGameClick(View view) {
        startNewGame();
    }

    private void startNewGame() {
        mGame.newGame();
        mDotsView.invalidate();
        updateMovesAndScore();
    }

    private void updateMovesAndScore() {
        mMovesRemaining.setText(String.format(Locale.getDefault(), "%d", mGame.getMovesLeft()));
        mScore.setText(String.format(Locale.getDefault(), "%d", mGame.getScore()));
    }
}