import sys
from auth import MiBand3

MAC_ADDR = sys.argv[1]
print 'Attempting to connect to ', MAC_ADDR

band = MiBand3(MAC_ADDR, debug=True)
band.setSecurityLevel(level="medium")


def l(x):
    print 'Realtime heart BPM:', x


# Authenticate the MiBand
if len(sys.argv) > 2:
    if band.initialize():
        print("Initialized...")
    band.disconnect()
    sys.exit(0)
else:
    band.authenticate()
    band.start_raw_data_realtime(heart_measure_callback=l)
