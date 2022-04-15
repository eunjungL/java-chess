# java-chess

체스 미션 저장소

## 4단계 - 웹 UI 구현 ♟

콘솔 UI 에서 체스 게임을 할 수 있는 기능을 구현한다.

### 웹 체스 구현 기능 목록
- [x] 웹 브라우저 에서 `Start` 버튼 클릭시 체스를 시작한다.
- [x] 웹 브라우저에서 체스판을 출력한다. 
- [x] 웹 브라우저에서 기물을 클릭하고 이동하고싶은 타일을 클릭한다.(WHITE 부터 시작)
- [X] 웹 브라우저에서 `End` 버튼 클릭시 체스를 종료하고 체스판이 초기화된다.
- [x] 웹 브라우저에서 `Status` 버튼 클릭시 각 팀의 점수와 승자를 출력한 후, 이어서 게임을 진행한다.
- [x] 웹 브라우저에서 `Load` 버튼 클릭시 이전에 진행 하던 게임을 불러온다.
- [x] 상대편 왕을 잡으면 더이상 게임이 진행되지 않는다.

### 구현 기능 목록
- [x] 게임 시작 혹은 종료 여부를 입력받는다.
    - [x] [예외처리] `start` 또는 `end` 이외의 문자 입력 시 예외 발생
- [x] 체스판을 생성한다.
    -  가로 위치는 왼쪽부터 a ~ h이고, 세로는 아래부터 위로 1 ~ 8로 구현한다.
  ```
    RNBQKBNR  8 (rank 8)
    PPPPPPPP  7
    ........  6
    ........  5
    ........  4
    ........  3
    pppppppp  2
    rnbqkbnr  1 (rank 1)
  
    abcdefgh
  ```
- [x] 체스판을 출력한다.
- [x] 기물이 움직일 위치를 입력받는다.
  - [x] [예외처리] `move source위치 target위치`(ex. `move b2 b3`) 이외의 문자 입력 시 예외 발생
  - [x] [예외처리] `source위치` 에 기물이 없을 시 예외 발생
  - [x] [예외처리] `source위치` 에 상대편 기물이 있을 시 예외 발생
  - [x] [예외처리] `target위치` 에 같은편 기물이 있을 시 예외 발생
  - [x] [예외처리] `target위치` 로 움직일 수 없을 시 예외 발생
- [x] 기물을 움직인다.
  - [x] Pawn
    - 이동 방법: 앞으로 한 칸 전진한다. 다만 첫 이동시에는 두 칸 전진할 수 있다.
    - 잡는 방법: 대각선으로 한 칸 앞에 있는 상대기물을 잡을 수 있다.
  - [x] Knight
    - 이동 방법: 한 방향으로 두 칸을 이동하고 그와 90도를 이루는 방향으로 한칸 이동한다. 다른 기물을 뛰어넘을 수 있다.
    - 잡는 방법: 이동한 위치의 상대기물을 잡을 수 있다.
  - [x] Bishop
    - 이동 방법: 대각선으로 원하는 만큼 이동할 수 있다. 다른 기물을 뛰어넘을 수 없다.
    - 잡는 방법 : 이동한 위치의 상대기물을 잡을 수 있다.
  - [x] Rook
    - 이동 방법: 전후좌우로 원하는 만큼 이동 할 수 있다. 다른 기물을 뛰어넘을 수 없다.
    - 잡는 방법: 이동한 위치의 상대기물을 잡을 수 있다.
  - [x] Queen
    - 이동 방법: 전후좌우, 대각선으로 원하는 만큼 이동 할 수 있다. 다른 기물을 뛰어넘을 수 없다.
    - 잡는 방법: 이동한 위치의 상대기물을 잡을 수 있다.
  - [x] King
    - 이동 방법: 전후좌우, 대각선으로 한칸 이동 할 수 있다.
    - 잡는 방법: 이동한 위치의 상대기물을 잡을 수 있다.
- [x] King이 잡혔을 때 게임을 종료한다.
- [x] `status` 입력 시 점수를 출력한다.
    - 각 말의 점수는 queen은 9점, rook은 5점, bishop은 3점, knight는 2.5점이다.
    - pawn의 기본 점수는 1점이다. 하지만 같은 세로줄에 같은 색의 폰이 있는 경우 1점이 아닌 0.5점을 준다.



### 실행 결과
  ```
  체스 게임을 시작합니다.
  게임 시작은 start, 종료는 end 명령을 입력하세요.
  start
  RNBQKBNR
  PPPPPPPP
  ........
  ........
  ........
  ........
  pppppppp
  rnbqkbnr
  
  end
```
