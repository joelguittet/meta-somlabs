DESCRIPTION = "u-boot port for SoMLabs boards"
SECTION = "bootloader"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

# Add compatible machines
COMPATIBLE_MACHINE = "sls16y2_792c_256r_256n_0sf|sls16y2_792c_256r_256n_1wb|sls16y2_792c_512r_04ge_0sf|sls16y2_792c_512r_04ge_1wb|sls16y2_792c_512r_512n_0sf|sls16y2_792c_512r_512n_1wb|sls16y2_792c_512r_sd_0sf|sls16y2_792c_512r_sd_1wb"

require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"
DEPENDS += "bison-native"

SRCBRANCH = "somlabs-imx_v2018.03_4.14.98_2.0.0_ga"
SRCREV = "dbe68c464c301ddcd83ca4c09d7bf93baf85ed5e"

SRC_URI = " \
    git://github.com/SoMLabs/somlabs-uboot-imx.git;protocol=git;branch=${SRCBRANCH} \
"

S = "${WORKDIR}/git"

inherit fsl-u-boot-localversion

LOCALVERSION ?= "-${SRCBRANCH}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Create symlink for usage of uuu provided by SoMLabs
do_deploy_append() {
    ln -sf u-boot-${type}-${PV}-${PR}.${UBOOT_SUFFIX} "vsom-6ull-fb.bin"
}
