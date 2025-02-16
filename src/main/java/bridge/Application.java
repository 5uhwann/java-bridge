package bridge;

import bridge.controller.BridgeGameController;

public class Application {

    public static void main(String[] args) {
        // TODO: 프로그램 구현
        BridgeMaker bridgeMaker = new BridgeMaker(new BridgeRandomNumberGenerator());
        BridgeGameController gameController = new BridgeGameController(bridgeMaker);
        gameController.run();
    }
}
