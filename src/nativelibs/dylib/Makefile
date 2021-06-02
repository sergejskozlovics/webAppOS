DLL := webmemory.so
JAVA_DIR := /Users/sergejs/jdk
INCLUDES := -I$(JAVA_DIR)/include -I$(JAVA_DIR)/include/linux -I$(JAVA_DIR)/darwin -I$(JAVA_DIR)/include/win32

LIBS := -L$(JAVA_DIR)/lib/server
SRC_DIR := .
OBJ_DIR := .

CPP_FILES := $(wildcard $(SRC_DIR)/*.cpp)
C_FILES := tda_unicode.c
SRC_FILES := $(CPP_FILES) $(C_FILES)
OBJ_FILES := $(patsubst $(SRC_DIR)/%.cpp,$(OBJ_DIR)/%.o,$(SRC_FILES))

$(DLL): $(OBJ_FILES)
	$(CXX) -shared -o $(DLL) $(OBJ_FILES) -lcurses -ljvm  $(LIBS)

%.o: %.cpp
	$(CXX) $(INCLUDES) -c $< -o $@

%.o: %.c
	$(CC) $(INCLUDES) -c $< -o $@
