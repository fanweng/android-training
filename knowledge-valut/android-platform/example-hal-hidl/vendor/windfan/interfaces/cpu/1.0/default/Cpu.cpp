Return<bool> Cpu::setScalingGovernor(const hidl_string& governor) {
	return write_value("/sys/devices/system/cpu/cpuo/cpufreq/scaling_governor", governor.c_str()) == 0;
}
