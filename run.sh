# inspired by github.com/alexpado/papermc-plugin-starter

### ENV Variables
# Relative paths, absolute ones would work too
PAPER_OUT_DIR="./output/paper/"
PAPER_WORKSPACE=".test/.papermc/"
VELOCITY_OUT_DIR="./output/velocity/"
VELOCITY_WORKSPACE=".test/.velocity/"
# shellcheck disable=SC2034
PAPER_VERSION="1.18"

ROOT_PATH=$(pwd)

# shellcheck disable=SC2120
function downloadPaper {
  if [[ -z "$PAPER_VERSION" ]]; then
    if [[ -z "$1" ]]; then
      echo "Please set PAPER_VERSION to the version group you want to use"
      exit 1
    fi
    PAPER_VERSION="$1"
  fi

  curl --silent "https://api.papermc.io/v2/projects/paper/version_group/$PAPER_VERSION/builds" | jq '.builds' > /tmp/builds.json

  if [[ -z "$PAPER_BUILD" ]]; then
    if [[ -z "$2" ]]; then
      jq '.[-1]' /tmp/builds.json > /tmp/paper.json
    fi
    PAPER_BUILD=$2
  fi

  if [[ -n "$PAPER_BUILD" ]]; then
    jq "'.[] | select(.build ==' $PAPER_BUILD ')" /tmp/builds.json /tmp.paper.json
  fi

  MC_VERSION=$(jq -r '.version' /tmp/paper.json)
  PAPER_BUILD=$(jq -r '.build' /tmp/paper.json)
  MC_NAME=$(jq -r '.downloads.application.name' /tmp/paper.json)

  echo "Cleaning up"
  rm /tmp/paper.json /tmp/builds.json

  echo "Downloading paper build $PAPER_BUILD for minecraft version $MC_VERSION"
  DL_URL="https://api.papermc.io/v2/projects/paper/versions/$MC_VERSION/builds/$PAPER_BUILD/downloads/$MC_NAME"
  curl --silent "$DL_URL" --output paper.jar

  echo "paperclip jar $MC_NAME downloaded"
}

# shellcheck disable=SC2120
function downloadVelocity {
  curl --silent "https://api.papermc.io/v2/projects/velocity/version_group/3.0.0/builds" | jq '.builds' > /tmp/builds.json

  if [[ -z "$VELOCITY_BUILD" ]]; then
    if [[ -z "$1" ]]; then
      jq '.[-1]' /tmp/builds.json > /tmp/velocity.json
    fi
    VELOCITY_BUILD=$1
  fi

  if [[ -n "$VELOCITY_BUILD" ]]; then
    jq "'.[] | select(.build ==' $VELOCITY_BUILD ')" /tmp/builds.json /tmp/velocity.json
  fi

  VELOCITY_VERSION=$(jq -r '.version' /tmp/velocity.json)
  VELOCITY_BUILD=$(jq -r '.build' /tmp/velocity.json)
  VELOCITY_NAME=$(jq -r '.downloads.application.name' /tmp/velocity.json)

  echo "Cleaning up"
  rm /tmp/builds.json /tmp/velocity.json

  echo "Downloading Velocity $VELOCITY_VERSION build $VELOCITY_BUILD"
  DL_URL="https://api.papermc.io/v2/projects/velocity/versions/$VELOCITY_VERSION/builds/$VELOCITY_BUILD/downloads/$VELOCITY_NAME"
  curl --silent "$DL_URL" --output velocity.jar

  echo "velocity jar $VELOCITY_NAME downloaded"
}

function setupServer {
  OLD_PATH=$(pwd)

  cd $PAPER_OUT_DIR || exit

  ABSOLUTE_PAPER_WORKSPACE="$OLD_PATH/$PAPER_WORKSPACE"
  PLUGINS_DIR="$ABSOLUTE_PAPER_WORKSPACE/plugins/"

  if [ ! -d "$PLUGINS_DIR" ]; then
    mkdir -p "$PLUGINS_DIR"
  fi

  # Copy all built plugins to the plugin folder of the paper server
  for file in *.jar; do
    cp "$file" "$PLUGINS_DIR"
  done;

  cd "$ABSOLUTE_PAPER_WORKSPACE" || exit

  if [[ ! -f "paper.jar" ]]; then
    # shellcheck disable=SC2119
    downloadPaper
    echo "java -Xmx2G -Xms2G -jar paper.jar nogui" >> start.sh
    chmod +x start.sh
    INTERVENTION=1
    echo "Downloaded paper.jar, you will still have to setup the server to communicate with the proxy"
  fi

  if [[ -z "$DISABLE_VELOCITY" ]]; then
    ABSOLUTE_VELOCITY_WORKSPACE="$OLD_PATH/$VELOCITY_WORKSPACE"
    PLUGINS_DIR="$ABSOLUTE_VELOCITY_WORKSPACE/plugins/"

    if [ ! -d "$PLUGINS_DIR" ]; then
      mkdir -p "$PLUGINS_DIR"
    fi

    cd "$OLD_PATH" || exit
    cd $VELOCITY_OUT_DIR || exit

    # Copy all built plugins to the plugin folder of the velocity proxy
    for file in *.jar; do
      cp "$file" "$PLUGINS_DIR"
    done;

    cd "$ABSOLUTE_VELOCITY_WORKSPACE" || exit

    if [[ ! -f "velocity.jar" ]]; then
      downloadVelocity
      echo "java -Xmx1G -Xms1G -jar velocity.jar" >> start.sh
      chmod +x start.sh
      INTERVENTION=1
      echo "Downloaded velocity.jar, you will still have to setup the proxy to communicate with the server"
    fi
  fi
}

# Check if workspace folder exists
if [ ! -d $PAPER_WORKSPACE ]; then
  mkdir -p $PAPER_WORKSPACE
fi

if [[ -z "$DISABLE_VELOCITY" ]]; then
  if [ ! -d $VELOCITY_WORKSPACE ]; then
    mkdir -p $VELOCITY_WORKSPACE
  fi
fi

./gradlew assemble

setupServer

cd "$ROOT_PATH" || exit

if [[ -n "$INTERVENTION" ]]; then
  # The server still needs to be set up manually, nothing we can do here
  exit 0
else
  # The server is already set up, so we can start it
  if [[ -z "$DISABLE_VELOCITY" ]]; then
    if [[ $1 == "velocity" ]]; then
      (trap 'kill 0' SIGINT EXIT; (cd $PAPER_WORKSPACE && ./start.sh) & (cd $VELOCITY_WORKSPACE && ./start.sh))
    else
      (trap 'kill 0' SIGINT EXIT; (cd $VELOCITY_WORKSPACE && ./start.sh) & (cd $PAPER_WORKSPACE && ./start.sh))
    fi
  else
    cd $PAPER_WORKSPACE || exit
    ./start.sh
  fi
fi