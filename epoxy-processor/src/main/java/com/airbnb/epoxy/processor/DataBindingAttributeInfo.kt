package com.airbnb.epoxy.processor

import androidx.room.compiler.processing.XMethodElement
import com.airbnb.epoxy.processor.Utils.removeSetPrefix

internal class DataBindingAttributeInfo(
    modelInfo: DataBindingModelInfo,
    setterMethod: XMethodElement,
    hashCodeValidator: HashCodeValidator,
    memoizer: Memoizer
) : AttributeInfo(memoizer) {

    init {
        fieldName = removeSetPrefix(setterMethod.name)
        // Use the memoizer parameter from the current round, not modelInfo.memoizer.
        // modelInfo may have been created in a previous round and its memoizer holds stale KSP references.
        setXType(setterMethod.parameters[0].type, memoizer)
        rootClass = modelInfo.generatedName.simpleName()
        packageName = modelInfo.generatedName.packageName()
        useInHash = !modelInfo.enableDoNotHash ||
            hashCodeValidator.implementsHashCodeAndEquals(xType)
        ignoreRequireHashCode = true
        generateSetter = true
        generateGetter = true
        hasFinalModifier = false
        isPackagePrivate = false
        isGenerated = true
    }
}
