import { StyleSheet } from 'react-native';

export default styles = StyleSheet.create({

    container: {
        flex: 1,
        justifyContent: "center",
        backgroundColor: "white"
    },

    powerButton: { 
        position: 'absolute',                                          
        bottom: 10,                                                    
        right: 10, 
    },

    powerButtonWithPopup: { 
        position: 'absolute',                                          
        bottom: 80,                                                    
        right: 10, 
    },

    powerButtonIcon: {
        height: 75,
        width: 75
    },

    powerLevelImage:{
        height: 25,
        width: 25
    },

    popupContent: {
        backgroundColor: "white",
        padding: 22,
        borderRadius: 4,
        borderColor: "black"
    },

    popupContainer: {
        padding: 16,
        justifyContent: "space-between",
        flexDirection: 'row',
    },

    popupPowerLevelRow: {
        justifyContent: "center",
        flexDirection: 'row',
    },

    leftPopupContainer: {
        alignItems: "flex-start",
    },

    rightPopupContainer: {
        alignItems: "flex-end",
    },

    popupContainerItem: {
        paddingTop: 8,
    },

    popupContainerItemHeader: {
        paddingTop: 8,
        fontSize: 14,
        fontWeight: "bold"
    },

    popupContainerItemCoordinates: {
        paddingTop: 8,
        color: "#9e9e9e"
    },

    popupContainerItemBatteryRedLevel: {
        paddingTop: 8,
        color: "#f44336"
    },

    popupContainerItemBatteryYellowLevel: {
        paddingTop: 8,
        color: "#fdcc01"
    },

    popupContainerItemBatteryGreenLevel: {
        paddingTop: 8,
        color: "#34da8d"
    },
    
});