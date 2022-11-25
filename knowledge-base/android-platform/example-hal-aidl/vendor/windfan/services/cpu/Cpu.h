#pragma once

#include <aidl/vendor/windfan/cpu/BnCpu.h>

namespace aidl::vendor::windfan::cpu {

class Cpu : public BnCpu {
  public:
    ndk::ScopedAStatus setScalingGovernor(const std::string& in_governor, bool* _aidl_return) override;
};

}  // namespace aidl::vendor::windfan::cpu
