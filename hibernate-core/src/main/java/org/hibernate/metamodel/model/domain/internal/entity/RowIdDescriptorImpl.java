/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */

package org.hibernate.metamodel.model.domain.internal.entity;

import java.sql.Types;

import org.hibernate.cfg.NotYetImplementedException;
import org.hibernate.metamodel.model.creation.spi.RuntimeModelCreationContext;
import org.hibernate.metamodel.model.domain.NavigableRole;
import org.hibernate.metamodel.model.domain.spi.BasicValueMapper;
import org.hibernate.metamodel.model.domain.spi.EntityHierarchy;
import org.hibernate.metamodel.model.domain.spi.ManagedTypeDescriptor;
import org.hibernate.metamodel.model.domain.spi.NavigableVisitationStrategy;
import org.hibernate.metamodel.model.domain.spi.RowIdDescriptor;
import org.hibernate.metamodel.model.relational.spi.DerivedColumn;
import org.hibernate.sql.SqlExpressableType;
import org.hibernate.type.descriptor.java.spi.BasicJavaDescriptor;
import org.hibernate.type.internal.ColumnBasedMapper;
import org.hibernate.type.spi.BasicType;

/**
 * @author Steve Ebersole
 */
public class RowIdDescriptorImpl<J> implements RowIdDescriptor<J> {
	private final EntityHierarchy hierarchy;
	// todo : really need to expose AbstractEntityPersister.rowIdName for this to work.
	//		for now we will just always assume a selection name of "ROW_ID"
	private final DerivedColumn column;
	private final BasicValueMapper valueMapper;

	public RowIdDescriptorImpl(
			EntityHierarchy hierarchy,
			RuntimeModelCreationContext creationContext) {
		this.hierarchy = hierarchy;
		this.column = new DerivedColumn(
				hierarchy.getRootEntityType().getPrimaryTable(),
				"ROW_ID",
				creationContext.getTypeConfiguration()
						.getSqlTypeDescriptorRegistry()
						.getDescriptor( Types.INTEGER ),
				creationContext.getTypeConfiguration(),
				(BasicJavaDescriptor) creationContext.getJavaTypeDescriptorRegistry()
						.getDescriptor( Integer.class.getName() ),
				true,
				true
		);
		this.valueMapper = new ColumnBasedMapper( column );
	}

	@Override
	public NavigableRole getNavigableRole() {
		// what should this be?
		throw new NotYetImplementedException(  );
	}

	@Override
	public BasicJavaDescriptor<J> getJavaTypeDescriptor() {
		// what should this be?
		throw new NotYetImplementedException(  );
	}

	@Override
	public String asLoggableText() {
		return NAVIGABLE_NAME;
	}

	@Override
	public ManagedTypeDescriptor getContainer() {
		return hierarchy.getRootEntityType();
	}

	@Override
	public void visitNavigable(NavigableVisitationStrategy visitor) {
		visitor.visitRowIdDescriptor( this );
	}

	@Override
	public String getNavigableName() {
		return NAVIGABLE_NAME;
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.BASIC;
	}

	@Override
	public DerivedColumn getBoundColumn() {
		return column;
	}

	@Override
	public BasicValueMapper<J> getValueMapper() {
		return valueMapper;
	}

	@Override
	public SqlExpressableType getSqlExpressableType() {
		return column.getExpressableType();
	}

	public BasicType<J> getBasicType() {
		return null;
	}
}