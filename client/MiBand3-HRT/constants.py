___all__ = ['UUIDS']


class Immutable(type):

    def __call__(*args):
        raise Exception("You can't create instance of immutable object")

    def __setattr__(*args):
        raise Exception("You can't modify immutable object")


class UUIDS(object):
    __metaclass__ = Immutable

    BASE = "0000%s-0000-1000-8000-00805f9b34fb"

    SERVICE_MIBAND1 = BASE % 'fee0'
    SERVICE_MIBAND2 = BASE % 'fee1'
    SERVICE_HEART_RATE = BASE % '180d'
    CHARACTERISTIC_AUTH = "00000009-0000-3512-2118-0009af100700"
    CHARACTERISTIC_HEART_RATE_MEASURE = "00002a37-0000-1000-8000-00805f9b34fb"
    CHARACTERISTIC_HEART_RATE_CONTROL = "00002a39-0000-1000-8000-00805f9b34fb"
    CHARACTERISTIC_SENSOR = "00000001-0000-3512-2118-0009af100700"

    NOTIFICATION_DESCRIPTOR = 0x2902


class AUTH_STATES(object):
    __metaclass__ = Immutable

    AUTH_OK = "Auth ok"
    AUTH_FAILED = "Auth failed"
    ENCRIPTION_KEY_FAILED = "Encryption key auth fail, sending new key"
    KEY_SENDING_FAILED = "Key sending failed"
    REQUEST_RN_ERROR = "Something went wrong when requesting the random number"


class QUEUE_TYPES(object):
    __metaclass__ = Immutable

    HEART = 'heart'
    RAW_ACCEL = 'raw_accel'
    RAW_HEART = 'raw_heart'
