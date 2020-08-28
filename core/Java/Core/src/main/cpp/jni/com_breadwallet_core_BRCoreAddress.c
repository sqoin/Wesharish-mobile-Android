//  Created by Ed Gamble on 1/23/2018
//  Copyright (c) 2018 Breadwinner AG.  All right reserved.
//
//  See the LICENSE file at the project root for license information.
//  See the CONTRIBUTORS file at the project root for a list of contributors.

#include <stdlib.h>
#include <assert.h>
#include "BRCoreJni.h"
#include "support/BRAddress.h"
#include "bcash/BRBCashAddr.h"
#include "com_breadwallet_core_BRCoreAddress.h"

/*
 * Class:     com_breadwallet_core_BRCoreAddress
 * Method:    createCoreAddress
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL
Java_com_breadwallet_core_BRCoreAddress_createCoreAddress
        (JNIEnv *env, jclass thisClass, jstring stringObject) {
    BRAddress *address = (BRAddress *) calloc (1, sizeof (BRAddress));

    // If given NULL, just return an empty address
    if ((*env)->IsSameObject (env, stringObject, NULL))
        return (jlong) address;

    // ... otherwise fill in address
    size_t stringLen = (size_t) (*env)->GetStringLength (env, stringObject);
    size_t stringLenMax = sizeof (address->s) - 1;

    // Do not overflow address->s
    if (stringLen > stringLenMax)
        stringLen = stringLenMax;

    const char *stringChars = (const char *) (*env)->GetStringUTFChars (env, stringObject, 0);
    memcpy(address->s, stringChars, stringLen);

    return (jlong) address;
}

/*
 * Class:     com_breadwallet_core_BRCoreAddress
 * Method:    createCoreAddressFromScriptPubKey
 * Signature: ([B)J
 */
JNIEXPORT jlong JNICALL Java_com_breadwallet_core_BRCoreAddress_createCoreAddressFromScriptPubKey
        (JNIEnv *env, jclass thisClass, jbyteArray scriptByteArray) {
    BRAddress *address = (BRAddress *) calloc (1, sizeof (BRAddress));

    size_t scriptLen = (size_t) (*env)->GetArrayLength (env, scriptByteArray);
    const uint8_t *script = (const uint8_t *) (*env)->GetByteArrayElements (env, scriptByteArray, 0);

    // TODO: Error handling
    BRAddressFromScriptPubKey(address->s, sizeof(address->s), script, scriptLen);

    return (jlong) address;
}

/*
 * Class:     com_breadwallet_core_BRCoreAddress
 * Method:    createCoreAddressFromScriptSignature
 * Signature: ([B)J
 */
JNIEXPORT jlong JNICALL Java_com_breadwallet_core_BRCoreAddress_createCoreAddressFromScriptSignature
        (JNIEnv *env, jclass thisClass, jbyteArray scriptByteArray) {
    BRAddress *address = (BRAddress *) calloc(1, sizeof(BRAddress));

    size_t scriptLen = (size_t) (*env)->GetArrayLength(env, scriptByteArray);
    const uint8_t *script = (const uint8_t *) (*env)->GetByteArrayElements(env, scriptByteArray, 0);

    // TODO: Error handling
    BRAddressFromScriptSig(address->s, sizeof(address->s), script, scriptLen);

    return (jlong) address;
}

/*
 * Class:     com_breadwallet_core_BRCoreAddress
 * Method:    stringify
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_com_breadwallet_core_BRCoreAddress_stringify
        (JNIEnv *env, jobject thisObject) {
    BRAddress *address = (BRAddress *) getJNIReference (env, thisObject);
    return (*env)->NewStringUTF (env, address->s);
}

/*
 * Class:     com_breadwallet_core_BRCoreAddress
 * Method:    isValid
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_com_breadwallet_core_BRCoreAddress_isValid
        (JNIEnv *env, jobject thisObject) {
    BRAddress *address = (BRAddress *) getJNIReference(env, thisObject);
    return (jboolean) (BRAddressIsValid(address->s)
                       ? JNI_TRUE
                       : JNI_FALSE);
}

/*
 * Class:     com_breadwallet_core_BRCoreAddress
 * Method:    getPubKeyScript
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL
Java_com_breadwallet_core_BRCoreAddress_getPubKeyScript
        (JNIEnv *env, jobject thisObject) {
    BRAddress *address = (BRAddress *) getJNIReference(env, thisObject);

    size_t pubKeyLen = BRAddressScriptPubKey(NULL, 0, address->s);
    uint8_t pubKey[pubKeyLen];
    BRAddressScriptPubKey(pubKey, pubKeyLen, address->s);

    jbyteArray result = (*env)->NewByteArray (env, (jsize) pubKeyLen);
    (*env)->SetByteArrayRegion (env, result, 0, (jsize) pubKeyLen, (const jbyte *) pubKey);

    return result;
}

/*
 * Class:     com_breadwallet_core_BRCoreAddress
 * Method:    bcashDecodeBitcoin
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_breadwallet_core_BRCoreAddress_bcashDecodeBitcoin
        (JNIEnv *env, jclass thisClass, jstring bcashAddrString ) {
    const char *bcashAddr = (*env)->GetStringUTFChars (env, bcashAddrString, 0);

    char bitcoinAddr[36 + 1];

    // returns the number of bytes written to bitcoinAddr36 (maximum of 36)
    size_t bitcoinAddrLen = BRBCashAddrDecode (bitcoinAddr, bcashAddr);
    bitcoinAddr[bitcoinAddrLen] = '\0';

    return (*env)->NewStringUTF (env, bitcoinAddr);
}

/*
 * Class:     com_breadwallet_core_BRCoreAddress
 * Method:    bcashEncodeBitcoin
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_breadwallet_core_BRCoreAddress_bcashEncodeBitcoin
        (JNIEnv *env, jclass thisClass, jstring bitcoinAddrString) {
    const char *bitcoinAddr = (*env)->GetStringUTFChars (env, bitcoinAddrString, 0);

    char bcashAddr[55 + 1];

    // returns the number of bytes written to bCashAddr55 (maximum of 55)

    size_t bcashAddrLen = BRBCashAddrEncode(bcashAddr, bitcoinAddr);
    bcashAddr[bcashAddrLen] = '\0';

    return (*env)->NewStringUTF (env, bcashAddr);
}
