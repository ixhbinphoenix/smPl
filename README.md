# smPl
smPl is the monorepo for the velocity and paper Plugins used for the Keltra minecraft server

## Building
Note: These plugins are not intended for private use.

To compile, run the following command in the root folder
```shell
./gradlew assemble
```

You can then copy either the paper plugins from the output/paper/ folder, or the velocity plugin from output/velocity

## Test setup (linux only)
Requires:
- curl
- jq
- java 17

To download and run test paper and velocity servers, run
```shell
./run.sh
```

Then, configure the velocity proxy (located in .test/.velocity) to use modern forwarding to the paper server (located in .test/.papermc) following [this guide](https://docs.papermc.io/velocity/player-information-forwarding) for 1.18.2 servers.

Currently the plugins also require a Postgres database to be running.
Run one yourself either natively on your machine, on a server or in a docker container and configure the plugins in their respective config.json and config.yml.
This requirement will probably be lifted from the paper plugins, but will remain for smProxy

After you have configured everything, you can run
```shell
./run.sh
```
again to compile the plugins, copy them over and start the paper server and velocity proxy

## License
- Copyright (C) 2022 ixhbinphoenix
- Copyright (C) 2022 renkertm

This Project is, unless mentioned otherwise, licensed under the GNU General Public License v3.0 which you can read [here](./LICENSE) or at [https://gnu.org/licenses](https://gnu.org/licenses)
