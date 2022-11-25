namespace aidl {
namespace vendor {
namespace windfan {
namespace cpu {
class BnCpu : public ::ndk::BnCInterface<ICpu> {
public:
    BnCpu();
    virtual ~BnCpu();
protected:
    ::ndk::SpAIBinder createBinder() override;
};
}
}
}
}
