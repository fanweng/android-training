void main() {
    android::sp<ICpu> cpuService = ICpu::getService();
    bool result = cpuService->setScalingGovernor(argv[1]);
}
