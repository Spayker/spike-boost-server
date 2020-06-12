import { StyleSheet } from 'react-native';

export default styles = StyleSheet.create({

    container:{
        flex: 1,
        justifyContent: "center",
        backgroundColor: "#060403",
        padding: 15,
    },

    contentTextDescription:{
        textAlign: "center",
        marginTop: 16,
        color: "#9e9e9e",
        fontSize: 16
    },

    image:{
        alignSelf: "center",
    },

    listTrainingContainer: {
        flex: 1,
        flexDirection: "row",
        alignItems: 'center',
        justifyContent: "space-between",
        backgroundColor: '#333333',
        borderRadius: 5,
        padding: 10,
        marginTop: 5
    },

    addTrainingButton: {
        marginTop: 10,
        height: 65,
        backgroundColor: "#158A15",
        borderRadius:30,
        alignItems: 'center'
    },

    discoverDevicesButtonText: {
        color: "#FFFFFF",
        height: 65,
        fontSize: 16,
        paddingTop: 20
    },

    listTrainingColumnData:{
        alignSelf: "center",
        flexDirection: "column",
    },

    item: {
        fontSize: 18,
        color: "white",
    }

});