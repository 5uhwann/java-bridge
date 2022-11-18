package bridge.domain.operator;

import static org.assertj.core.api.Assertions.*;

import bridge.controller.GameStatus;
import bridge.domain.bridge.Bridge;
import bridge.domain.player.Player;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BridgeGameTest {

    @Test
    @DisplayName("플레이어가 선택한 칸으로 이동시키고 이동 결과를 저장한다.")
    void moveTest() {
        //given
        Bridge bridge = new Bridge(List.of("0", "0", "1"));
        BridgeGame bridgeGame = new BridgeGame(bridge, new Player(), new GameStatus());

        String playerSelection1 = "D";
        String playerSelection2 = "U";

        //when
        bridgeGame.move(playerSelection1);
        bridgeGame.move(playerSelection2);

        //then
        List<String> upBridgeResult = bridgeGame.getBridgeResult().getUpBridge();
        List<String> downBridgeResult = bridgeGame.getBridgeResult().getDownBridge();

        assertThat(upBridgeResult.get(0)).isEqualTo("   ");
        assertThat(upBridgeResult.get(1)).isEqualTo(" X ");
        assertThat(downBridgeResult.get(0)).isEqualTo(" O ");
        assertThat(downBridgeResult.get(1)).isEqualTo("   ");
    }

    @Nested
    @DisplayName("플레이어 이동 후 변화는")
    class HandleAfterMoveTest {

        @Test
        @DisplayName("플레이어가 건널 수 없는 칸으로 이동한 것이라면 상태는 실패이며, 게임 시도 횟수가 증가한다.")
        void case1() {
            //given
            Bridge bridge = new Bridge(List.of("0", "0", "1"));
            Player player = new Player();
            BridgeGame bridgeGame = new BridgeGame(bridge, player, new GameStatus());

            BridgeResult bridgeResult = bridgeGame.getBridgeResult();
            int beforeMoveAttempt = bridgeResult.getAttempt();

            String playerSelection = "U";

            //when
            bridgeGame.move(playerSelection);

            //then
            int afterMoveAttempt = bridgeResult.getAttempt();
            assertThat(player.isCross()).isEqualTo(false);
            assertThat(afterMoveAttempt).isEqualTo(beforeMoveAttempt + 1);
        }

        @Test
        @DisplayName("플레이어가 건널 수 있는 칸으로 이동한 것이라면 상태는 성공이며, 게임 시도 횟수는 변화 없다.")
        void case2() {
            //given
            Bridge bridge = new Bridge(List.of("0", "0", "1"));
            Player player = new Player();
            BridgeGame bridgeGame = new BridgeGame(bridge, player, new GameStatus());

            BridgeResult bridgeResult = bridgeGame.getBridgeResult();
            int beforeMoveAttempt = bridgeResult.getAttempt();

            String playerSelection = "D";

            //when
            bridgeGame.move(playerSelection);

            //then
            int afterMoveAttempt = bridgeResult.getAttempt();
            assertThat(player.isCross()).isEqualTo(true);
            assertThat(afterMoveAttempt).isEqualTo(beforeMoveAttempt);
        }
    }

    @Nested
    @DisplayName("플레이어의 게임 성공 여부는")
    class IsClearTest {

        @Test
        @DisplayName("플레이어가 다리 끝까지 건너면 성공이다.")
        void case1() {
            //given
            Bridge bridge = new Bridge(List.of("0", "0", "1"));

            Player player = new Player();
            player.movePlayerLocation();
            player.movePlayerLocation();
            player.movePlayerLocation();

            GameStatus gameStatus = new GameStatus();

            BridgeGame bridgeGame = new BridgeGame(bridge, player, gameStatus);

            //when
            boolean clearResult = bridgeGame.isClear();

            //then
            assertThat(clearResult).isEqualTo(true);
            assertThat(gameStatus.isSuccess()).isEqualTo(true);
        }

        @Test
        @DisplayName("플레이어가 다리 끝까지 건너지 못하면 실패이다.")
        void case2() {
            //given
            Bridge bridge = new Bridge(List.of("0", "0", "1"));

            Player player = new Player();
            player.movePlayerLocation();
            player.movePlayerLocation();

            GameStatus gameStatus = new GameStatus();

            BridgeGame bridgeGame = new BridgeGame(bridge, player, gameStatus);

            //when
            boolean clearResult = bridgeGame.isClear();

            //then
            assertThat(clearResult).isEqualTo(false);
            assertThat(gameStatus.isSuccess()).isEqualTo(false);
        }
    }
}
