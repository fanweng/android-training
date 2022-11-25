#ifndef VENDOR_WINDFAN_CPU_V1_0_CPU_H
#define VENDOR_WINDFAN_CPU_V1_0_CPU_H

#include <vendor/windfan/cpu/1.0/ICpu.h>

namespace vendor {
namespace windfan {
namespace cpu {
namespace V1_0 {
namespace implementation {

using ::android::hardware::hidl_string;
using ::android::hardware::Return;
using ::android::hardware::Void;
using ::android::sp;
using ::vendor::windfan::cpu::V1_0::ICpu;

struct Cpu : public ICpu {
public:
    // Methods from ::vendor::windfan::cpu::V1_0::ICpu follow
    Return<bool> setScalingGovernor(const hidl_string& governor) override;
};

extern "C" ICpu* HIDL_FETCH_ICpu(const char* name);

}  // namespace implementation
}  // namespace V1_0
}  // namespace cpu
}  // namespace windfan
}  // namespace vendor

#endif
