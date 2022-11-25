#define LOG_TAG "vendor.windfan.cpu-service"

#include <android-base/logging.h>
#include <android/binder_process.h>
#include <android/binder_manager.h>
#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>

#include "Cpu.h"

using aidl::vendor::windfan::cpu::Cpu;
using std::string_literals::operator""s;

int main() {
    // Enable vndbinder to allow vendor-to-vendor binder calls.
    android::ProcessState::initWithDriver("/dev/vndbinder");

    ABinderProcess_startThreadPool();

    LOG(INFO) << "Cpu aidl service for windfan is starting.,,";

    std::shared_ptr<Cpu> cpu = ndk::SharedRefBase::make<Cpu>();
    const std::string name = Cpu::descriptor + "/default"s;
    CHECK_EQ(STATUS_OK, AServiceManager_addService(cpu->asBinder().get(), name.c_str()));

    LOG(INFO) << "Initialized windfan cpu";

    ABinderProcess_joinThreadPool();

    return EXIT_FAILURE;  // should not reach
}
