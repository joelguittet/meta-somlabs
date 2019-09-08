meta-somlabs
==

SoMLabs Yocto meta layer.

This layer contains kernel and u-boot recipes to flash the SoMLabs boards.

This layer depends on the additional mandatory layers:
* meta-poky
* meta-yocto-bsp
* meta-openembedded/meta-oe
* meta-openembedded/meta-python
* meta-openembedded/meta-networking
* meta-freescale

Examples to use this layer are available in my Github at https://github.com/myfreescalewebpage/meta-somlabs-examples.

**Important note: VisionSOM-6ULL modules are all created in the meta-layer but only sls16y2_792c_512r_512n_0sf, sls16y2_792c_512r_512n_1wb and sls16y2_792c_512r_sd_0sf machines are tested. I'm not a SoMLabs employee, I haven't all modules myself. If anyone would like to help flashing and running images for the other machines, feedback and/or fixes are welcome.**


Machines
--

The following machines are supported in this meta layer:
* sls16y2_792c_256r_256n_0sf: VisionSOM-6ULL with 256MB RAM, 256MB NAND
* sls16y2_792c_256r_256n_1wb: VisionSOM-6ULL with 256MB RAM, 256MB NAND, Wi-Fi + Bluetooth Murata 1DX module
* sls16y2_792c_512r_512n_0sf: VisionSOM-6ULL with 512MB RAM, 512MB NAND
* sls16y2_792c_512r_512n_1wb: VisionSOM-6ULL with 512MB RAM, 512MB NAND, Wi-Fi + Bluetooth Murata 1DX module
* sls16y2_792c_512r_04ge_0sf: VisionSOM-6ULL with 512MB RAM, 4GB eMMC
* sls16y2_792c_512r_04ge_1wb: VisionSOM-6ULL with 512MB RAM, 4GB eMMC, Wi-Fi + Bluetooth Murata 1DX module
* sls16y2_792c_512r_sd_0sf: VisionSOM-6ULL with 512MB RAM, uSD
* sls16y2_792c_512r_sd_1wb: VisionSOM-6ULL with 512MB RAM, uSD, Wi-Fi + Bluetooth Murata 1DX module

The linux-fslc-imx-somlabs recipe build device tree based on the name of the machine: sls16y2_792c_256r_256n_0sf.dts used for sls16y2_792c_256r_256n_0sf machine, etc. It is possible to override the default device tree source in another layer with a higher priority to customize the target, or you can create a new machine and a new device tree that match it.


Using
--

The following tutorial is useful to start building your own Yocto project and loading your SoMLabs board. The development machine is running Ubuntu 16.04.

**_1- Install System Dependencies (once)_**

	sudo apt-get update && sudo apt-get upgrade
	sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat libsdl1.2-dev xterm lzop u-boot-tools git build-essential curl libusb-1.0-0-dev python-pip minicom android-tools-fsutils android-tools-fastboot libncurses5-dev
	sudo pip install --upgrade pip && sudo pip install pyserial

**_2- Get sources and flashing tools (once)_**

Clone sources:

	git clone --branch warrior git://git.yoctoproject.org/poky.git ~/yocto/poky
	git clone --branch warrior git://git.openembedded.org/meta-openembedded ~/yocto/meta-openembedded
	git clone --branch warrior https://github.com/Freescale/meta-freescale.git ~/yocto/meta-freescale
	git clone --branch warrior https://github.com/myfreescalewebpage/meta-somlabs.git ~/yocto/meta-somlabs

Get SoMLabs tools:

	git clone --branch warrior https://github.com/myfreescalewebpage/somlabs-tools ~/yocto/somlabs-tools

Create images directory:

	mkdir -p ~/yocto/images

Copy uuu to images directory:

	cp ~/yocto/somlabs-tools/uuu ~/yocto/images/uuu

**_3- Configure build (once)_**

Setup environnement:

	cd ~/yocto
	source poky/oe-init-build-env

Add layers to the configuration file ~/yocto/build/conf/bblayers.conf:

	BBLAYERS ?= " \
	  ${TOPDIR}/../poky/meta \
	  ${TOPDIR}/../poky/meta-poky \
	  ${TOPDIR}/../poky/meta-yocto-bsp \
	  ${TOPDIR}/../meta-openembedded/meta-oe \
	  ${TOPDIR}/../meta-openembedded/meta-python \
	  ${TOPDIR}/../meta-openembedded/meta-networking \
	  ${TOPDIR}/../meta-freescale \
	  ${TOPDIR}/../meta-somlabs \
	"

Set the right machine in the configuration file ~/yocto/build/conf/local.conf, depending of the expected target, for example:

	MACHINE ??= "sls16y2_792c_512r_512n_1wb"

**_4- Restore environnement (when restarting the development machine)_**

Restore environnement:

        cd ~/yocto
        source poky/oe-init-build-env

**_5- Build_**

Build minimal image:

	cd ~/yocto/build
	bitbake core-image-minimal

**_6- Flash target_**

### Machines with NAND device (sls16y2_792c_256r_256n_0sf, sls16y2_792c_256r_256n_1wb, sls16y2_792c_512r_512n_0sf, sls16y2_792c_512r_512n_1wb)

Copy files in the images directory (replace core-image-minimal-sls16y2_792c_512r_512n_1wb.ubi by the wanted rootfs if you have build another image):

	cp ~/yocto/build/tmp/deploy/images/sls16y2_792c_512r_512n_1wb/core-image-minimal-sls16y2_792c_512r_512n_1wb.ubi ~/yocto/images/rootfs.ubi
	cp ~/yocto/build/tmp/deploy/images/sls16y2_792c_512r_512n_1wb/vsom-6ull-fb.bin ~/yocto/images/vsom-6ull-fb.bin

Then start the target in SERIAL mode, with first USB port connected to your computer. If using the VisionSOM-6ULL-STD carrier board, you can refer to the following images.

![SERIAL](https://github.com/myfreescalewebpage/somlabs-tools/blob/warrior/visionsom-6ull-std-serial-mode.jpg)

![USB](https://github.com/myfreescalewebpage/somlabs-tools/blob/warrior/visionsom-6ull-std-usb-uuu.jpg)

Flash the target:

	cd ~/yocto/images/
	sudo ./uuu -b vsom_6ull_nand_flasher vsom-6ull-fb.bin rootfs.ubi

Logs are displayed in the terminal and on the serial console interface (SERIAL0) of the target to check the progression and the verification of the flashing procedure.

At the end of the flashing procedure, the NAND device is flashed. Disconnect the power supply and set the target in INTERNAL mode. If using the VisionSOM-6ULL-STD carrier board, you can refer to the following image.

![INTERNAL](https://github.com/myfreescalewebpage/somlabs-tools/blob/warrior/visionsom-6ull-std-internal-mode.jpg)

Restart the target. The console is available on SERIAL0 pins of the board. Speed is 115200. Login is 'root' with no password.

### Machines with eMMC device (sls16y2_792c_512r_04ge_0sf, sls16y2_792c_512r_04ge_1wb)

Copy image file in the images directory (replace core-image-minimal-sls16y2_792c_512r_04ge_1wb.wic by the wanted rootfs if you have build another image):

	cp ~/yocto/build/tmp/deploy/images/sls16y2_792c_512r_04ge_1wb/core-image-minimal-sls16y2_792c_512r_04ge_1wb.wic ~/yocto/images/rootfs.wic

Then start the target in SERIAL mode, with first USB port connected to your computer. If using the VisionSOM-6ULL-STD carrier board, you can refer to the following images.

![SERIAL](https://github.com/myfreescalewebpage/somlabs-tools/blob/warrior/visionsom-6ull-std-serial-mode.jpg)

![USB](https://github.com/myfreescalewebpage/somlabs-tools/blob/warrior/visionsom-6ull-std-usb-uuu.jpg)

Flash the target:

	cd ~/yocto/images/
	sudo ./uuu -b vsom_6ull_emmc_flasher rootfs.wic

Logs are displayed in the terminal and on the serial console interface (SERIAL0) of the target to check the progression and the verification of the flashing procedure.

At the end of the flashing procedure, the eMMC device is flashed. Disconnect the power supply and set the target in INTERNAL mode. If using the VisionSOM-6ULL-STD carrier board, you can refer to the following image.

![INTERNAL](https://github.com/myfreescalewebpage/somlabs-tools/blob/warrior/visionsom-6ull-std-internal-mode.jpg)

Restart the target. The console is available on SERIAL0 pins of the board. Speed is 115200. Login is 'root' with no password.

### Machines with SD card (sls16y2_792c_512r_sd_0sf, sls16y2_792c_512r_sd_1wb)

Copy image file in the images directory (replace core-image-minimal-sls16y2_792c_512r_sd_1wb.wic by the wanted rootfs if you have build another image):

	cp ~/yocto/build/tmp/deploy/images/sls16y2_792c_512r_sd_1wb/core-image-minimal-sls16y2_792c_512r_sd_1wb.wic ~/yocto/images/rootfs.wic

Flash the SD card (replace sdX by the right SD card device):

	cd ~/yocto/images/
	sudo dd if=rootfs.wic of=/dev/sdX bs=1M

At the end of the flashing procedure, the SD card is flashed. Insert the SD card in the VisionSOM-6ULL module and set the target in INTERNAL mode. If using the VisionSOM-6ULL-STD carrier board, you can refer to the following image.

![INTERNAL](https://github.com/myfreescalewebpage/somlabs-tools/blob/warrior/visionsom-6ull-std-internal-mode.jpg)

Restart the target. The console is available on SERIAL0 pins of the board. Speed is 115200. Login is 'root' with no password.


Contributing
--

All contributions are welcome :-)

Use Github Issues to report anomalies or to propose enhancements (labels are available to clearly identify what you are writing) and Pull Requests to submit modifications.


References
--

* https://somlabs.com/
