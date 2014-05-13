/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.builder.impl;

import java.util.List;

import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.ExtensionDefinition;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;
import org.opendaylight.yangtools.yang.model.api.Status;
import org.opendaylight.yangtools.yang.model.api.UnknownSchemaNode;
import org.opendaylight.yangtools.yang.parser.builder.api.AbstractSchemaNodeBuilder;

import com.google.common.collect.ImmutableList;

public final class ExtensionBuilder extends AbstractSchemaNodeBuilder {
    private ExtensionDefinitionImpl instance;
    private String argument;
    private boolean yin;

    ExtensionBuilder(final String moduleName, final int line, final QName qname, final SchemaPath path) {
        super(moduleName, line, qname);
        this.schemaPath = path;
    }

    @Override
    public ExtensionDefinition build() {
        if (instance != null) {
            return instance;
        }

        instance = new ExtensionDefinitionImpl(qname, schemaPath);
        instance.argument = argument;
        instance.yin = yin;

        // UNKNOWN NODES
        for (UnknownSchemaNodeBuilder b : addedUnknownNodes) {
            unknownNodes.add(b.build());
        }
        instance.unknownNodes = ImmutableList.copyOf(unknownNodes);

        return instance;
    }

    public void setYinElement(boolean yin) {
        this.yin = yin;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    @Override
    public String toString() {
        return "extension " + qname.getLocalName();
    }

    private static final class ExtensionDefinitionImpl implements ExtensionDefinition {
        private final QName qname;
        private String argument;
        private final SchemaPath schemaPath;
        private String description;
        private String reference;
        private Status status;
        private ImmutableList<UnknownSchemaNode> unknownNodes;
        private boolean yin;

        private ExtensionDefinitionImpl(QName qname, SchemaPath path) {
            this.qname = qname;
            this.schemaPath = path;
        }

        @Override
        public QName getQName() {
            return qname;
        }

        @Override
        public SchemaPath getPath() {
            return schemaPath;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getReference() {
            return reference;
        }

        @Override
        public Status getStatus() {
            return status;
        }

        @Override
        public List<UnknownSchemaNode> getUnknownSchemaNodes() {
            return unknownNodes;
        }

        @Override
        public String getArgument() {
            return argument;
        }

        @Override
        public boolean isYinElement() {
            return yin;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((qname == null) ? 0 : qname.hashCode());
            result = prime * result + ((schemaPath == null) ? 0 : schemaPath.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ExtensionDefinitionImpl other = (ExtensionDefinitionImpl) obj;
            if (qname == null) {
                if (other.qname != null) {
                    return false;
                }
            } else if (!qname.equals(other.qname)) {
                return false;
            }
            if (schemaPath == null) {
                if (other.schemaPath != null) {
                    return false;
                }
            } else if (!schemaPath.equals(other.schemaPath)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(ExtensionDefinitionImpl.class.getSimpleName());
            sb.append("[");
            sb.append("argument=" + argument);
            sb.append(", qname=" + qname);
            sb.append(", schemaPath=" + schemaPath);
            sb.append(", extensionSchemaNodes=" + unknownNodes);
            sb.append(", yin=" + yin);
            sb.append("]");
            return sb.toString();
        }
    }

}
