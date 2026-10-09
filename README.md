# 2021년 대학교 비주얼 프로그래밍(Visual Programming) 과목 텀 프로젝트
> Java Swing 및 AWT 기반의 2D 플랫포머 점프 액션 게임

---

## 1. 프로젝트 개요 (Overview)

- **프로젝트명**: HoPoP (HoPoPProject)
- **개발 기간**: 2021년 1학기
- **제작 배경**: 대학교 컴퓨터공학 전공 '비주얼 프로그래밍' 수업 과제
- **개발 환경**: 
  - **IDE**: Eclipse IDE
  - **언어 / 런타임**: Java (JDK 8 이상 권장)
  - **GUI 라이브러리**: Java Swing (`JFrame`, `JPanel`), Java AWT (`Graphics2D`)
- **게임 장르**: 2D 타일 기반 플랫폼 점프 게임 (Platformer Jump Game)

플레이어가 발판을 밟고 위로 높이 뛰어오르며 위쪽 목표를 향해 올라가는 클래식 2D 플랫폼 점프 액션 게임입니다. 외부 게임 엔진(Unity 등)을 사용하지 않고, Java의 순수 그래픽 라이브러리와 객체지향 설계를 통해 게임 루프, 물리 충돌, 타일맵 파싱, 애니메이션을 바닥부터 직접 구현하였습니다.

---

## 2. 게임 플레이 및 조작 방법 (Controls)

### 메뉴 화면 (Title Menu)
- `↑` / `↓` 방향키 : 메뉴 이동 (`Start`, `Quit`)
- `Enter` : 메뉴 선택

### 인게임 조작 (In-Game Controls)
| 조작키 | 동작 |
| :--- | :--- |
| `←` / `→` | 플레이어 좌우 이동 |
| `Space` | 점프 (Jump) |
| `↑` / `↓` | 사다리/상하 조작 및 디버그용 입력 |

---

## 3. 시스템 구조 및 주요 기능 (Architecture & Features)

```
HoPoPProject/
├── src/
│   ├── main/
│   │   ├── Game.java              # 애플리케이션 진입점 (JFrame 생성 및 기본 윈도우 설정)
│   │   └── GamePanel.java         # 더블 버퍼링, Runnable 게임 루프(60 FPS), 키 입력 리스너
│   ├── GameState/
│   │   ├── GameState.java         # 게임 상태 추상 클래스
│   │   ├── GameStateManager.java  # 상태 머신 (MenuState, jumpmap_state 전환 관리)
│   │   ├── MenuState.java         # 메인 메뉴 화면 로직
│   │   └── jumpmap_state.java     # 점프 맵 메인 인게임 플레이 상태
│   ├── Entity/
│   │   ├── MapObject.java         # 충돌 박스, 물리 계산, 좌표 관리를 담당하는 최상위 오브젝트
│   │   ├── Player.java            # 플레이어 이동/점프 물리 엔진 및 상태 애니메이션 제어
│   │   └── Animation.java         # 스프라이트 시트 기반 프레임 애니메이션 처리기
│   ├── TileMap/
│   │   ├── Tile.java              # 타일 속성(NORMAL, BLOCKED) 및 타일 이미지 보유
│   │   ├── TileMap.java           # .map 파일 로더, 타일셋 스프라이트 분할, 카메라 스크롤(Tweening)
│   │   └── Background.java        # 배경 이미지 렌더링 및 패럴랙스 스크롤 연산
│   └── Audio/
│       └── AudioPlayer.java       # Java Sound API를 활용한 BGM/SFX 재생 모듈
└── Resources/
    ├── Backgrounds/               # 배경 이미지 리소스 (.png, .jpg)
    ├── Maps/                      # 텍스트 기반 타일맵 데이터 (jumpmap.map)
    ├── Sprites/Player/            # 플레이어 스프라이트 시트 (playersprites.png)
    └── Tilesets/                  # 블록 및 지형 타일셋 (grasstileset.png)
```

### 핵심 기능
1. **커스텀 60 FPS 게임 루프 (Game Loop)**
   - `Runnable` 인터페이스 기반 스레드에서 `update()` → `draw()` → `drawToScreen()` 사이클을 유지하며 `Thread.sleep()`을 통한 정밀 프레임 조절.
2. **타일맵 & 충돌 감지 시스템 (Tile Collision)**
   - 텍스트 파일(`jumpmap.map`)의 2차원 배열 데이터를 로드하여 지형을 동적으로 생성.
   - 사각형 경계(`cwidth`, `cheight`)의 모서리(4-Corner Detection)를 검사하여 `BLOCKED` 타일과의 상하좌우 충돌 및 보정 처리.
3. **물리 엔진 (Physics Simulation)**
   - 가속도(`moveSpeed`), 감속/마찰력(`stopSpeed`), 최고 속도(`maxSpeed`), 중력/낙하 가속(`fallSpeed`), 점프 초속(`jumpStart`)을 반영한 관성 물리 구현.
4. **스프라이트 시트 애니메이션 (Sprite Animation)**
   - 정지(IDLE), 걷기(WALKING), 점프(JUMPING), 낙하(FALLING) 상태에 따른 스프라이트 분할 및 프레임 딜레이 조절.
5. **카메라 트래킹 (Camera Tweening)**
   - 플레이어 위치를 추적하며 화면 중심에 오도록 부드러운 카메라 보간(Tweening) 처리.

---

## 4. 프로젝트의 한계점 및 개선점 (Limitations)

학부 수업 과제로 단기간 내에 핵심 기능을 구현한 프로젝트로, 다음과 같은 기술적 한계와 아쉬운 점이 존재합니다.

1. **게임 클리어(Clear) 조건 및 플로우 미완성**
   - 최상단 목적지에 도달했을 때 승리 판정, 점수 정산, 혹은 다음 스테이지로 이어지는 완전한 클리어 시퀀스가 구현되지 않았습니다.
   - 코드 상에 디버그용으로 `VK_UP` 입력 시 `Clear!!` 텍스트를 임시 출력하는 방식이 남아 있어 정상적인 게임 완료 흐름이 부재합니다.

2. **배경(Background) 이미지 스크롤의 한계**
   - 세로로 올라가는 점프 맵임에도 불구하고 `Background` 클래스가 가로(X축) 스크롤 순환만을 염두에 두고 작성되었습니다.
   - 플레이어가 Y축(상단)으로 높이 이동할 때 배경 이미지의 상하 반복/순환(Wrap-around) 처리가 온전하지 않아 배경이 잘리거나 부자연스럽게 표현됩니다.

3. **게임 오버(Game Over) 및 낙하 처리 미비**
   - 아래로 떨어졌을 때의 체력 감소, 낙사 사망 판정, 리스폰(Respawn), 타이틀 화면 복귀 등의 페널티 처리가 완성되어 있지 않습니다.

4. **오디오 리소스(BGM / SFX) 누락**
   - 코드 상(`AudioPlayer`)에서는 `/Music/bgm.mp3`, `/SFX/jump.mp3`를 참조하고 있으나, 리소스 패키지에 실제 음원 파일이 누락되어 있어 실행 시 콘솔에 예외가 발생하거나 사운드가 출력되지 않습니다.

5. **단일 맵 및 콘텐츠 부족**
   - 30×30 크기의 단일 맵(`jumpmap.map`)만 존재하며, 적 몬스터(Enemy), 아이템, 기믹 등의 오브젝트가 다양하게 추가되지 못했습니다.

---

## 5. 실행 방법 (How to Run)

### Eclipse IDE에서 실행하기
1. **Eclipse 실행** 후 `File` > `Open Projects from File System...` 선택.
2. `HoPoPProject` 폴더를 루트 디렉토리로 지정하여 임포트.
3. `src` 및 `Resources` 폴더가 **Source Folder**로 등록되어 있는지 확인 (`Build Path` 확인).
4. `src/main/Game.java` 파일을 우클릭 → `Run As` > `Java Application` 실행.
