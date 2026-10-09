# 2021년 대학교 비주얼 프로그래밍(Visual Programming) 과목 텀 프로젝트
> Java Swing 및 AWT 기반의 2D 플랫포머 점프 액션 게임

---

## 1. 프로젝트 개요 (Overview)

- **프로젝트명**: HoPoP (HoPoPProject)
- **개발 기간**: 2021년 1학기
- **제작 배경**: 대학교 컴퓨터공학 전공 '비주얼 프로그래밍' 수업 과제
- **개발 환경**: 
  - **IDE**: VSCODE
  - **언어 / 런타임**: Java (JDK 8 이상 권장)
  - **GUI 라이브러리**: Java Swing (`JFrame`, `JPanel`), Java AWT (`Graphics2D`)
- **게임 장르**: 2D 타일 기반 플랫폼 점프 게임 (Platformer Jump Game)

플레이어가 발판을 밟고 위로 높이 뛰어오르며 위쪽 목표를 향해 올라가는 클래식 2D 플랫폼 점프 액션 게임입니다. 외부 게임 엔진(Unity 등)을 사용하지 않고, Java의 순수 그래픽 라이브러리와 객체지향 설계를 통해 게임 루프, 물리 충돌, 타일맵 파싱, 애니메이션을 바닥부터 직접 구현하였습니다.

---

## 1. Description

### 1.1 개요 및 개발 목적
**HoPoP**은 Java 언어의 객체지향 설계(OOP) 원칙과 Swing/AWT 그래픽 라이브러리를 활용하여 제작된 2D 버티컬 점프 액션 플랫포머(Jump Action Platformer) 게임입니다.  
본 프로젝트는 **컴퓨터 프로그래밍 및 게임 소프트웨어 공학 학습 과제**의 일환으로 수행되었으며, 고전적인 게임 루프(Game Loop), 유한 상태 머신(FSM) 기반의 상태 관리, 2D 타일맵 충돌 판정 물리 엔진, 패럴랙스 배경 스크롤, 크로스 플랫폼 빌드 시스템 구축을 직접 설계 및 구현하여 소프트웨어 개발 역량을 종합적으로 입증하고자 하였습니다.

### 1.2 게임 목표 및 규칙
- **목표**: 150행(세로 4,500픽셀)에 달하는 거대한 수직 타워형 맵을 점프하여 최상층의 목표 지점에 세워진 **깃발(Flag, 타일 12)**에 도달하는 것입니다.
- **게임 플레이**:
  - 발판 간의 높낮이와 간격을 계산하여 정확한 타이밍에 점프를 수행해야 합니다.
  - 최상층 도달 시 깃발과의 충돌을 실시간 감지하여 스테이지 클리어(`Clear!!`) 상태로 전이됩니다.

### 1.3 팀 구성 및 역할 분담 (Team & Roles)

본 프로젝트는 4인 협업 팀으로 개발되었으며, 세부적인 역할 분담과 기여도는 다음과 같습니다:

- **lee byeonghan (Project Manager & Core Developer)**
  - **Project Management (PM)**:
    - 프로젝트 전체 개발 마일스톤(Milestone) 수립 및 스프린트 일정 관리
    - 게임 핵심 요구사항 정의 및 시스템 기능 명세서 작성
    - 크로스 플랫폼 빌드 파이프라인(Makefile) 구축 및 환경 통합
  - **Core Development (개발)**:
    - **타일맵 시스템 전반 개발 (`TileMap`)**: 2D 타일 매트릭스 파싱, 뷰포트 컬링(Viewport Culling) 렌더링 최적화, 150층 버티컬 점프맵 레벨 디자인 설계, 배열 인덱스 경계 검사(Bounds Check) 방어 로직 및 최상층 깃발(타일 12) 접촉 트리거 판정 구현
    - **캐릭터 이동 및 물리 메커니즘 개발 (`Player` & `MapObject`)**: 가속도·최대속도·마찰력 기반의 정밀 수평 이동 컨트롤 구현, 중력 가속도 수치 적분을 통한 점프 물리 궤적 모델링, AABB 4-Corner 타일 충돌 검사 및 캐릭터 FSM 모션 상태 전이 연동
---

### 1.4 시스템 아키텍처 및 코드 구조 분석

프로젝트는 유지보수성과 확장성을 위해 모듈별로 명확히 역할이 분리된 패키지 구조로 설계되었습니다.

```
Hopop_Edit/
├── src/
│   ├── Main/               # 진입점 및 게임 루프 관리
│   │   ├── Game.java       # JFrame 윈도우 생성 및 초기화
│   │   └── GamePanel.java  # 메인 스레드, 고정 60 FPS 게임 루프, 더블 버퍼링
│   ├── GameState/          # 상태 패턴(State Pattern) 기반 화면 전이
│   │   ├── GameState.java        # 추상 기본 상태 클래스
│   │   ├── GameStateManager.java # 상태 전이 및 이벤트 라우팅 FSM
│   │   ├── MenuState.java        # 시작 메뉴 화면
│   │   └── jumpmap_state.java    # 인게임 점프맵 플레이 화면
│   ├── Entity/             # 게임 오브젝트 및 물리 엔진
│   │   ├── MapObject.java  # 물리 좌표, 속도/가속도, AABB 타일 충돌 판정
│   │   ├── Player.java     # 플레이어 FSM 모션, 입력 처리, 깃발 접촉 판정
│   │   └── Animation.java  # 스프라이트 프레임 타이머 및 순환 재생기
│   ├── TileMap/            # 타일 그리드 및 배경 렌더링 시스템
│   │   ├── Tile.java       # 타일 속성 정의 (NORMAL: 통과 가능, BLOCKED: 충돌체)
│   │   ├── TileMap.java    # 타일 파싱, 뷰포트 컬링(Viewport Culling) 최적화
│   │   └── Background.java # 비율 기반 2축 패럴랙스(Parallax) 스크롤 엔진
│   └── Audio/              # 사운드 시스템
│       └── AudioPlayer.java # Java Sound API 및 외부 SPI 기반 MP3 스트리밍
├── Resources/              # 그래픽, 맵 데이터, BGM/SFX 사운드 리소스
├── Libs/                   # MP3 디코딩 외부 의존성 JAR
└── Makefile                # 멀티플랫폼 컴파일 및 패키징 빌드 자동화 스크립트
```

---

### 1.5 핵심 클래스별 상세 설계 및 구현 원리

#### 1) `Main.GamePanel` (게임 엔진 및 루프)
- **고정 주기 게임 루프 (Fixed-rate Game Loop)**: `Runnable` 인터페이스 기반의 전용 게임 스레드를 가동하여 60 FPS(`targetTime = 1000 / 60 ≈ 16.6ms`)를 정밀하게 유지하며, `System.nanoTime()`을 기반으로 델타 타임을 계산하여 프레임 드랍 발생 시에도 일정한 물리 연산을 보장합니다.
- **더블 버퍼링 (Double Buffering)**: 캔버스 크기(`WIDTH: 230`, `HEIGHT: 373`)의 오프스크린 이미지(`BufferedImage`)에 먼저 모든 그래픽을 렌더링한 후, 화면 출력 시 `SCALE(2x)`로 확대 투사하여 깜빡임(Flickering) 현상을 원천 방지했습니다.

#### 2) `GameState.GameStateManager` (상태 관리 패턴)
- GoF 디자인 패턴 중 **상태 패턴(State Pattern)**을 적용하여 메뉴 화면(`MenuState`)과 실제 인게임(`jumpmap_state`)의 생명주기를 분리했습니다.
- 입력 이벤트(Key Event), 갱신 로직(`update()`), 렌더링 로직(`draw()`)을 활성화된 상태 객체로 위임(Delegation)하여 결합도를 낮추었습니다.

#### 3) `Entity.MapObject` & `Player` (물리 및 충돌 엔진)
- **운동 역학 구현**:
  - 수평 운동: 마찰력(`stopSpeed: 0.4`), 최대 속도 제한(`maxSpeed: 1.6`), 가속도(`moveSpeed: 0.3`) 반영.
  - 수직 운동: 중력 가속도(`fallSpeed: 0.15`), 종단 속도(`maxFallSpeed: 4.0`), 초기 점프력(`jumpStart: -4.8`)을 수치 적분하여 자연스러운 포물선 점프 궤적을 구현.
- **AABB(Axis-Aligned Bounding Box) 타일 충돌 검사**:
  - 플레이어의 충돌 박스(`cwidth: 20`, `cheight: 20`) 모서리 4점(Top-Left, Top-Right, Bottom-Left, Bottom-Right)을 타일 좌표계로 변환(`calculateCorners`)하여 벽 통과 방지 및 바닥 착지 판정을 수행합니다.
- **트리거 충돌 (Trigger Collision)**:
  - 깃발(타일 12)과 같은 비차단형(NORMAL) 목표 오브젝트에 대해서는 플레이어 경계 상자와의 교차 검사(`checkFlagCollision()`)를 수행하여 통과 시 클리어 플래그(`clear = true`)를 활성화합니다.

#### 4) `TileMap.TileMap` (뷰포트 컬링 최적화)
- 150행 × 30열에 달하는 대형 맵 전체를 매 프레임 렌더링하면 심각한 성능 저하가 발생하므로, 화면 시야에 들어오는 범위(`numRowsToDraw = HEIGHT / tileSize + 2`)의 타일만 연산하는 **뷰포트 컬링(Viewport Culling)**을 적용하여 성능을 최적화했습니다.

#### 5) `TileMap.Background` (정밀 패럴랙스 스크롤)
- 플레이어의 세로 좌표 진행률에 비례하여 7,680픽셀 길이의 배경 이미지를 매핑하는 선형 보간 알고리즘을 설계했습니다.

$$\text{progressY} = \frac{-\text{mapY}}{\text{mapHeight} - \text{screenHeight}}$$

$$\text{bgY} = -\text{progressY} \times (\text{imgHeight} - \text{screenHeight})$$

- 이를 통해 시작 지점(바닥)에서는 지상 풍경이 보이고, 점프하여 정상에 다다를수록 하늘/우주 배경으로 자연스럽게 전환됩니다.

---

### 1.6 학습 과정에서 해결한 주요 기술적 과제 (Troubleshooting)

1. **타일맵 크기 확장에 따른 경계 검사(Index Bounds Checking) 예외 해결**
   - **문제**: 기존 30행 맵을 150행으로 확장 시, 플레이어 순간이동이나 카메라 오프셋 이동 과정에서 음수 인덱스 또는 배열 초과가 발생하여 `ArrayIndexOutOfBoundsException`으로 게임이 강제 종료되는 문제 발생.
   - **해결**: `TileMap.getType()` 및 `draw()` 메서드에 엄격한 인덱스 유효성 검사(Bounds Check)를 적용하고, 경계 밖 참조 시 안전한 기본값(`BLOCKED` 또는 패스)을 반환하도록 방어적 프로그래밍을 구축했습니다.

2. **물리 파라미터 기반 맵 레벨 디자인 정합성 확보**
   - **문제**: 발판 사이의 세로 높이 차이가 너무 크면 플레이어의 점프력 한계로 인해 물리적으로 도달할 수 없는 버그 발생.
   - **해결**: 중력($g=0.15$)과 초기 점프 속도($v_0=-4.8$)의 물리 방정식을 적분하여 플레이어의 최대 도달 고도가 **79.2픽셀(약 2.64개 타일)**임을 수학적으로 도출했습니다. 이를 바탕으로 모든 발판 간 수직 단차가 **최대 2칸 이하(1~2칸)**가 되도록 맵 알고리즘을 재설계하여 100% 클리어 가능한 레벨 디자인을 완성했습니다.

3. **초장축 배경 렌더링 시 화면 찢어짐(Tearing) 및 공백 결함 해결**
   - **문제**: 640px 크기의 작은 배경을 사용하면서 150행(4,500px) 맵 진행 시 Y축 래핑 미지원으로 인해 화면 하단이 검게 잘리는 현상 발생.
   - **해결**: 미사용 에셋이었던 7,680px 초대형 `bground.jpg`를 발굴하여 연결하고, 맵 높이와 배경 높이 간의 스케일을 비율 연동하는 2축 패럴랙스 알고리즘을 도입하여 전 구간에서 결함 없는 연속적 배경을 구현했습니다.

4. **IDE 독립적인 Windows/Linux 통합 Make 빌드 자동화**
   - **문제**: Eclipse 의존적이던 프로젝트를 VS Code 및 터미널 환경에서 실행할 때 클래스패스 라이브러리(`Libs/*`)와 리소스 경로(`Resources/`) 인식 문제, Windows 환경변수 미반영 이슈 발생.
   - **해결**: 시스템 레지스트리의 `JAVA_HOME`을 자동 탐지하여 `javac`/`java`를 바인딩하고, 소스 컴파일과 리소스 동기화, Runnable JAR 생성까지 단일 명령(`make`, `make jar`)으로 완결되는 크로스 플랫폼 `Makefile`을 제작했습니다.

---

## 2. Instructions

### 2.1 개발 및 실행 환경
- **Language**: Java 17 (Microsoft OpenJDK 17 LTS 호환)
- **Build Tool**: GNU Make 4.x
- **Development Tool**: Visual Studio Code (권장 확장: `vscjava.vscode-java-pack`, `ms-vscode.makefile-tools`)

### 2.2 빌드 및 실행 명령어

터미널(Git Bash, PowerShell, CMD 등)에서 프로젝트 루트로 이동 후 다음 명령어를 실행합니다:

| 명령어 | 설명 |
| :--- | :--- |
| `make` / `make run` | 소스 컴파일 및 리소스 동기화 후 게임을 즉시 실행합니다 (기본) |
| `make build` | `.java` 파일 컴파일 및 `Resources/` 폴더 동기화만 수행합니다 |
| `make clean` | `bin/` 폴더 내 컴파일된 클래스 및 복사된 리소스를 모두 정리합니다 |
| `make jar` | 어디서나 단독 실행 가능한 `Hopop.jar` 파일을 생성합니다 |
| `make help` | Makefile의 지원 명령어 목록을 출력합니다 |

#### VS Code 내 원클릭 실행 안내
- **F5** 또는 **Ctrl + F5**: 내장 Java 런처를 통해 GUI 디버깅 모드로 게임 실행 (`.vscode/launch.json`)
- **Ctrl + Shift + B**: 단축키로 기본 빌드 작업(`make`) 자동 호출 (`.vscode/tasks.json`)

### 2.3 게임 조작법 (Controls)

| 조작키 | 기능 |
| :--- | :--- |
| `←` / `→` (방향키) | 플레이어 좌 / 우 이동 (가속도 및 마찰력 적용) |
| `Spacebar` (스페이스) | 점프 (도약 사운드 출력 및 포물선 점프 궤적) |
| `↑` / `↓` (방향키) | 시작 메뉴 화면에서 커서 이동 (Start / Quit) |
| `Enter` (엔터) | 시작 메뉴 선택 확정 |

---

## 3. Resources

프로젝트를 구성하는 에셋 파일의 세부 명세 및 기술적 역할입니다.

### 3.1 그래픽 에셋 (Graphics)
- **`Resources/Backgrounds/bground.jpg`**
  - 규격: 360 × 7,680 픽셀 (RGB JPEG)
  - 역할: 150행 맵 전반에 걸쳐 사용되는 초고해상도 패럴랙스 배경 (지상 지층부터 구름, 상공, 성층권까지 연속 표현)
- **`Resources/Backgrounds/hopop.png`**
  - 규격: 360 × 640 픽셀 (RGBA PNG)
  - 역할: 시작 메뉴 화면(`MenuState`)의 타이틀 일러스트레이션
- **`Resources/Tilesets/grasstileset.png`**
  - 규격: 630 × 60 픽셀 (RGBA PNG, 30×30 타일 단위 총 42개)
  - 구성: 
    - 0행(상단 21개): 통과 가능한 타일 (`Tile.NORMAL`), 풀/꽃 장식(9, 10), 깃발(12)
    - 1행(하단 21개): 충돌 판정이 있는 타일 (`Tile.BLOCKED`), 흙 블록(21), 벽돌 벽(22), 발판(23, 24, 25)
- **`Resources/Sprites/Player/playersprites.png`**
  - 규격: 30 × 30 픽셀 단위 프레임 애니메이션 시트
  - 동작 구성: 대기(Idle: 2프레임), 걷기(Walking: 8프레임), 점프(Jumping: 1프레임), 낙하(Falling: 2프레임)

### 3.2 맵 데이터 (Map Data)
- **`Resources/Maps/jumpmap.map`**
  - 규격: ASCII 텍스트 (1행: 열 수 `30`, 2행: 행 수 `150`, 3~152행: 타일 번호 매트릭스)
  - 특징: 플레이어의 점프 도달 한계(2.64타일)를 반영하여 행 간격이 엄격하게 $\le 2$칸으로 유지된 150층 버티컬 코스, 최상층(행 3)에 깃발(`10 12 10`) 배치

### 3.3 오디오 에셋 (Audio)
- 저작권으로 인해 github버전에선 삭제
- **`Resources/Music/bgm.mp3`**: 인게임 메인 BGM (반복 재생 루프)
- **`Resources/SFX/jump.mp3`**: 스페이스바 점프 시 출력되는 효과음

### 3.4 외부 라이브러리 (Dependencies)
표준 Java Sound API는 MP3 스트림 디코딩을 기본 지원하지 않으므로, 다음 오픈소스 라이브러리를 클래스패스로 바인딩하여 무손실 재생을 지원합니다:
- **`Libs/jl1.0.1.jar` (JLayer)**: 순수 Java로 작성된 MPEG 1.0/2.0 Layer III 오디오 디코더
- **`Libs/mp3spi1.9.5.jar` (MP3 SPI)**: Java Sound Service Provider Interface(SPI)와 JLayer를 연결하는 플러그인
- **`Libs/tritonus_share.jar`**: Tritonus 오픈소스 오디오 아키텍처 공용 유틸리티 패키지
