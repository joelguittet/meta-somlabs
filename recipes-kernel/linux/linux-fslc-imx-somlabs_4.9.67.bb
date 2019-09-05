DESCRIPTION = "Linux Kernel for SoMLabs boards"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

# Add compatible machines
COMPATIBLE_MACHINE = "sls16y2_792c_256r_256n_0sf|sls16y2_792c_256r_256n_1wb|sls16y2_792c_512r_04ge_0sf|sls16y2_792c_512r_04ge_1wb|sls16y2_792c_512r_512n_0sf|sls16y2_792c_512r_512n_1wb|sls16y2_792c_512r_sd_0sf|sls16y2_792c_512r_sd_1wb"

require recipes-kernel/linux/linux-yocto.inc

DEPENDS += "lzop-native bc-native"

SRCBRANCH = "4.9-1.0.x-imx"
SRCREV = "953c6e30c9701fda69ef08e2476c541dc4fb1453"

SRC_URI += " \
    git://github.com/Freescale/linux-fslc.git;protocol=git;branch=${SRCBRANCH} \
    file://defconfig \
    file://sls16xx-base.dtsi \
    file://sls16xx-nand.dtsi \
    file://sls16xx-emmc.dtsi \
    file://sls16xx-sd.dtsi \
    file://sls16xx-bluetooth-murata-1dx.dtsi \
    file://sls16xx-wifi-murata-1dx.dtsi \
    file://sls16y2_792c_256r_256n_0sf.dts \
    file://sls16y2_792c_256r_256n_1wb.dts \
    file://sls16y2_792c_512r_04ge_0sf.dts \
    file://sls16y2_792c_512r_04ge_1wb.dts \
    file://sls16y2_792c_512r_512n_0sf.dts \
    file://sls16y2_792c_512r_512n_1wb.dts \
    file://sls16y2_792c_512r_sd_0sf.dts \
    file://sls16y2_792c_512r_sd_1wb.dts \
"

S = "${WORKDIR}/git"

LINUX_VERSION ?= "4.9.67"
LINUX_VERSION_EXTENSION ?= "-fslc"
PV = "${LINUX_VERSION}+git${SRCPV}"

# We need to pass it as param since kernel might support more than one machine, with different entry points
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

# Copy all dts/dtsi files and create machine dts symlink after unpack of the sources
python do_copydts () {
    import glob
    import shutil
    import os
    src = ("%s/*.dtsi" % (d.getVar('WORKDIR')))
    dst = ("%s/arch/%s/boot/dts/" % (d.getVar('S'), d.getVar('ARCH')))
    for file in glob.glob(src):
        shutil.copy2(file, dst)
    src = ("%s/*.dts" % (d.getVar('WORKDIR')))
    dst = ("%s/arch/%s/boot/dts/" % (d.getVar('S'), d.getVar('ARCH')))
    for file in glob.glob(src):
        shutil.copy2(file, dst)
    src = ("%s/%s.dts" % (d.getVar('WORKDIR'), d.getVar('MACHINE')))
    dst = ("%s/arch/%s/boot/dts/%s" % (d.getVar('S'), d.getVar('ARCH'), d.getVar('KERNEL_DEVICETREE').replace('dtb', 'dts')))
    os.symlink(src, dst)
}
addtask do_copydts after do_unpack before do_patch
