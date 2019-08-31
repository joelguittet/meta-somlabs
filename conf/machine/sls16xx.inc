# SoMLabs SLS16 (VisionSOM-6UL and VisionSOM-6ULL) common board definition file

include conf/machine/include/imx-base.inc
include conf/machine/include/tune-cortexa7.inc

# Compatibility with mx6 machines
MACHINEOVERRIDES =. "mx6:mx6ull:"

# u-boot and linux version
PREFERRED_PROVIDER_u-boot = "u-boot-somlabs"
PREFERRED_PROVIDER_virtual/bootloader = "u-boot-somlabs"
PREFERRED_PROVIDER_virtual/kernel = "linux-fslc-imx-somlabs"

# u-boot configurations
UBOOT_CONFIG[sd] = "somlabs_visionsom_6ull_fastboot_defconfig"
UBOOT_CONFIG[emmc] = "somlabs_visionsom_6ull_fastboot_defconfig"
UBOOT_CONFIG[nand] = "somlabs_visionsom_6ull_fastboot_defconfig"

# Console
SERIAL_CONSOLE = "115200 ttymxc0"

# Add kernel in image
IMAGE_INSTALL_append = " kernel-image kernel-devicetree kernel-modules"

# Always depend of u-boot
EXTRA_IMAGEDEPENDS += "u-boot-somlabs"

# Image types, tar.bz2 for convenience
IMAGE_FSTYPES = "tar.bz2"
