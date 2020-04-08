/*
 * Copyright (c) 2018 Pantheon Technologies, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.data.util.codec;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.annotations.Beta;
import java.util.Optional;
import java.util.function.Function;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.QNameModule;
import org.opendaylight.yangtools.yang.model.api.IdentitySchemaNode;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.type.IdentityrefTypeDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for implementing string-to-identity codecs.
 *
 * @author Robert Varga
 */
@Beta
@NonNullByDefault
public final class IdentityCodecUtil {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityCodecUtil.class);

    private IdentityCodecUtil() {
        // Hidden on purpose
    }

    /**
     * Parse a string into a QName using specified prefix-to-QNameModule mapping function, interpreting the result
     * as an IdentitySchemaNode existing in specified SchemaContext.
     *
     * @param value string value to parse
     * @param schemaContext Parent schema context
     * @param prefixToModule prefix-to-QNameModule mapping function
     * @return Corresponding IdentitySchemaNode.
     * @throws IllegalArgumentException if the value is invalid or does not refer to an existing identity
     */
    public static IdentitySchemaNode parseIdentity(final String value, final SchemaContext schemaContext,
            final IdentityrefTypeDefinition type, final Function<String, QNameModule> prefixToModule) {
        final QName qname = QNameCodecUtil.decodeQName(value, prefixToModule);
        Optional<Module> optModule = schemaContext.findModule(qname.getModule());
        checkState(optModule.isPresent(), "Parsed QName %s refers to a non-existent module", qname);

        /*
        Set<IdentitySchemaNode> identities = type.getIdentities();
        if (type != null) {
            optModule = schemaContext.findModule(type.getQName().getModule());
            checkState(optModule.isPresent(), "QName %s refers to a non-existent module", qname);
        }
        */

        LOG.debug("Identity type: {}", type);
        LOG.debug("Module of identity type: {}", optModule.get());
        LOG.debug("Matching {} ...", qname);
        for (IdentitySchemaNode identity : optModule.get().getIdentities()) {
            LOG.debug("Against: {}", identity.getQName());
            if (qname.equals(identity.getQName())) {
                return identity;
            }

            /*
            for (IdentitySchemaNode derived : identity.getDerivedIdentities()) {
                LOG.info("Against: {}", derived.getQName());
                if (qname.equals(derived.getQName())) {
                    return identity;
                }
            }
            */
        }

        throw new IllegalArgumentException("Parsed QName " + qname + " does not refer to a valid identity");
    }
}
