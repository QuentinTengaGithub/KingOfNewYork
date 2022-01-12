package fr.epita.koh.game

class GameState {
    private val players = arrayOf(
        Player(true),
        Player(false),
        Player(false),
        Player(false));

    private var currentPlayerTurn = -1

    init {
        setPlayerTurn(0);
    }

    private fun setPlayerTurn(newTurn : Int) {
        println(newTurn);

        if (currentPlayerTurn >= 0)
            players[currentPlayerTurn].playerState = PlayerState.WaitingTurn;

        currentPlayerTurn = newTurn;
        players[currentPlayerTurn].playerState = PlayerState.RollDice;
    }

    private fun setPlayerStage(newStage : PlayerState) {
        players[currentPlayerTurn].playerState = newStage;
    }

    private fun nextPlayerStage() {
        when (players[currentPlayerTurn].playerState) {
            PlayerState.RollDice -> setPlayerStage(PlayerState.EnterNewYork);
            PlayerState.EnterNewYork -> setPlayerStage(PlayerState.BuyPowerCards);
            PlayerState.BuyPowerCards -> setPlayerStage(PlayerState.EndOfTurn);
            PlayerState.EndOfTurn -> setPlayerTurn((currentPlayerTurn + 1) % players.size);
        }
    }

    fun reset() {
        currentPlayerTurn = -1;

        for (p in players)
            p.reset();

        setPlayerTurn(0);
    }

    fun play() {
        if (players[currentPlayerTurn].play())
            nextPlayerStage();
    }
}