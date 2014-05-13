/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.builder.api;

import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;
import org.opendaylight.yangtools.yang.model.api.Status;

/**
 * Interface for all builders of SchemaNode nodes.
 */
public interface SchemaNodeBuilder extends Builder {

    /**
     * Get qname of this node.
     *
     * @return QName of this node
     */
    QName getQName();

    /**
     * Get schema path of this node.
     *
     * @return SchemaPath of this node
     */
    SchemaPath getPath();

    /**
     * Set path to this node.
     *
     * @param path
     */
    void setPath(SchemaPath path);

    /**
     * Get description of this node.
     *
     * @return description statement
     */
    String getDescription();

    /**
     * Set description to this node.
     *
     * @param description
     */
    void setDescription(String description);

    /**
     * Get reference of this node.
     *
     * @return reference statement
     */
    String getReference();

    /**
     * Set reference to this node.
     *
     * @param reference
     */
    void setReference(String reference);

    /**
     * Get status of this node.
     *
     * @return status statement
     */
    Status getStatus();

    /**
     * Set status to this node.
     *
     * @param status
     */
    void setStatus(Status status);

    /**
     * Build SchemaNode object from this builder.
     */
    SchemaNode build();

}
