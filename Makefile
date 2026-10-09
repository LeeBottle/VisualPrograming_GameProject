# ==============================================================================
# Makefile for HoPoP Java Game (Windows & VSCode 호환)
# ==============================================================================

# 자바 홈 경로 자동 탐지 (터미널 환경변수 -> Windows 레지스트리 -> 기본 설치 경로)
DETECTED_JAVA_HOME := $(JAVA_HOME)
ifeq ($(strip $(DETECTED_JAVA_HOME)),)
    DETECTED_JAVA_HOME := $(shell powershell -NoProfile -ExecutionPolicy Bypass -Command "[Environment]::GetEnvironmentVariable('JAVA_HOME', 'Machine')")
endif
ifeq ($(strip $(DETECTED_JAVA_HOME)),)
    ifneq ($(wildcard C:/Program?Files/Microsoft/jdk-17.0.20.101-hotspot/bin/javac.exe),)
        DETECTED_JAVA_HOME := C:/Program Files/Microsoft/jdk-17.0.20.101-hotspot
    endif
endif

# 자바 실행 바이너리 지정
ifneq ($(strip $(DETECTED_JAVA_HOME)),)
    CLEAN_JAVA_HOME := $(patsubst %/,%,$(subst \,/,$(strip $(DETECTED_JAVA_HOME))))
    JAVAC := "$(CLEAN_JAVA_HOME)/bin/javac.exe"
    JAVA  := "$(CLEAN_JAVA_HOME)/bin/java.exe"
    JAR   := "$(CLEAN_JAVA_HOME)/bin/jar.exe"
else
    JAVAC := javac
    JAVA  := java
    JAR   := jar
endif

# 디렉터리 설정
SRC_DIR := src
BIN_DIR := bin
LIB_DIR := Libs
RES_DIR := Resources

# 메인 실행 클래스
MAIN_CLASS := Main.Game

# 클래스패스 (Windows는 세미콜론 ';' 구분자 사용)
CP_BUILD := "$(LIB_DIR)/*"
CP_RUN   := "$(BIN_DIR);$(LIB_DIR)/*;$(RES_DIR)"

# 소스 파일 목록 (src 하위 모든 java 파일 자동 탐색)
SOURCES := $(wildcard $(SRC_DIR)/*.java $(SRC_DIR)/*/*.java $(SRC_DIR)/*/*/*.java)

# PowerShell 명령어 (CMD / Git Bash 등 모든 Windows 터미널에서 호환)
PS := powershell -NoProfile -ExecutionPolicy Bypass -Command

# 기본 타겟: 빌드 후 실행
.PHONY: all run build compile copy-resources clean jar help

all: run

# 컴파일 및 리소스 복사
build: compile copy-resources

compile:
	@echo "[1/2] Compiling Java source files..."
	@$(PS) "if (-not (Test-Path '$(BIN_DIR)')) { New-Item -ItemType Directory -Path '$(BIN_DIR)' | Out-Null }"
	$(JAVAC) -encoding UTF-8 -cp $(CP_BUILD) -d $(BIN_DIR) $(SOURCES)
	@echo "[1/2] Compilation successful!"

# 리소스 파일을 bin 디렉터리로 복사
copy-resources:
	@echo "[2/2] Copying resources to $(BIN_DIR)..."
	@$(PS) "if (Test-Path '$(RES_DIR)') { Copy-Item -Path '$(RES_DIR)\*' -Destination '$(BIN_DIR)' -Recurse -Force }"
	@echo "[2/2] Resources copied!"

# 빌드 후 게임 실행
run: build
	@echo "Launching HoPoP..."
	$(JAVA) -cp $(CP_RUN) $(MAIN_CLASS)

# 빌드 결과물 정리
clean:
	@echo "Cleaning build artifacts in $(BIN_DIR)..."
	@$(PS) "if (Test-Path '$(BIN_DIR)') { Remove-Item -Path '$(BIN_DIR)\*' -Recurse -Force }"
	@echo "Clean completed!"

# 단독 실행 가능한 JAR 파일 생성 (bin 디렉터리 기준)
jar: build
	@echo "Creating executable JAR (Hopop.jar)..."
	$(JAR) cfe Hopop.jar $(MAIN_CLASS) -C $(BIN_DIR) .
	@echo "Hopop.jar created! Run with: java -cp \"Hopop.jar;Libs/*\" $(MAIN_CLASS)"

# 도움말
help:
	@echo "--------------------------------------------------"
	@echo "HoPoP Make Command Guide:"
	@echo "  make           - 빌드 후 게임을 실행합니다 (기본)"
	@echo "  make run       - 빌드 후 게임을 실행합니다"
	@echo "  make build     - 소스 코드를 컴파일하고 리소스를 복사합니다"
	@echo "  make clean     - bin 폴더의 빌드 결과물을 모두 삭제합니다"
	@echo "  make jar       - Hopop.jar 파일을 생성합니다"
	@echo "  make help      - 사용 가능한 명령어 목록을 표시합니다"
	@echo "--------------------------------------------------"
