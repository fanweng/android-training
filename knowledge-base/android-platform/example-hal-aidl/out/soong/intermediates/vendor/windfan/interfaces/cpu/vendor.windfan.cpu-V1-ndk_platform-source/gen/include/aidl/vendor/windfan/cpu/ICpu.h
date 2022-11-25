namespace aidl {
namespace vendor {
namespace windfan {
namespace cpu {
class ICpu : public ::ndk::ICInterface {
public:
    static const char* descriptor;
    ICpu();
    virtual ~ICpu();
    virtual ::ndk::ScopedAStatus setScalingGovernor(const std::string& in_governor, bool* _aidl_return) = 0;
    // ... ...
};

class ICpuDefault : public ICpu {
public:
    ::ndk::ScopedAStatus setScalingGovernor(const std::string& in_governor, bool* _aidl_return) override;
    ::ndk::SpAIBinder asBinder() override;
    bool isRemote() override;
};
}
}
}
}
