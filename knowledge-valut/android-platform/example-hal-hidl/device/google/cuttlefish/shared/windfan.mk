DEVICE_FRAMEWORK_COMPATIBILITY_MATRIX_FILE := device/google/cuttlefish/shared/device_framework_matrix.xml

# hidl_interface
PRODUCT_PACKAGES += vendor.windfan.cpu@1.0

# cc_binary (start by init)
PRODUCT_PACKAGES += vendor.windfan.cpu@1.0-service

# cc_binary
PRODUCT_PACKAGES_DEBUG += windfan_cpu_tester

# cc_library_shared
PRODUCT_PACKAGES += libwindfan

# java_sdk_library
PRODUCT_PACKAGES += com.windfan

# android app
PRODUCT_PACKAGES += WindfanServices
