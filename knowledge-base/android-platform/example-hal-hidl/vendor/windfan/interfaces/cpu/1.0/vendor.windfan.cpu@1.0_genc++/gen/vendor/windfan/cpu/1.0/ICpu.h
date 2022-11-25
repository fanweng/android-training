struct ICpu : public ::android::hidl::base::V1_0::IBase {
//... ...
    virtual ::android::hardware::Return<bool> setScalingGovernor(const ::android::hardware::hidl_string& governor) = 0;
};
