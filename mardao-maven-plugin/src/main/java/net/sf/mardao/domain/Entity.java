package net.sf.mardao.domain;

/*
 * #%L
 * net.sf.mardao:mardao-maven-plugin
 * %%
 * Copyright (C) 2010 - 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.mardao.dao.Cached;
import net.sf.mardao.plugin.ProcessDomainMojo;

/**
 * The domain object for Entities in the class graph.
 * 
 * @author f94os
 * 
 */
public class Entity implements Comparable<Entity> {
    private Class clazz;
    private String                   tableName;
    private Field                    parent;
    private Field                    pk;
    private final Set<Field>         fields            = new TreeSet<Field>();
    private final Set<Field>         oneToOnes         = new TreeSet<Field>();
    private final Set<Field>         manyToOnes        = new TreeSet<Field>();
    private final Set<Field>         manyToManys       = new TreeSet<Field>();
    private final List<Set<String>>  uniqueConstraints = new ArrayList<Set<String>>();
    private final Map<String, Field> mappedBy          = new HashMap<String, Field>();
    private final Set<Entity>        dependsOn         = new TreeSet<Entity>();
    private List<Entity>             ancestors         = new ArrayList<Entity>();
    private List<Entity>             parents           = new ArrayList<Entity>();
    private final Set<Entity>        children          = new TreeSet<Entity>();
    private Field                    geoLocation;
    private Field                    createdDate;
    private Field                    createdBy;
    private Field                    updatedDate;
    private Field                    updatedBy;
    private Cached                   cached;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
    
    public String getClassName() {
        return clazz.getName();
    }

    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    public String getSimpleLower() {
        return ProcessDomainMojo.firstToLower(clazz.getSimpleName());
    }

    public Field getPk() {
        return pk;
    }

    public void setPk(Field pk) {
        this.pk = pk;
    }

    public Set<Field> getFields() {
        return fields;
    }

    public Set<Field> getManyToOnes() {
        return manyToOnes;
    }

    public List<Set<String>> getUniqueConstraints() {
        return uniqueConstraints;
    }

    public boolean isUnique(String fieldName) {
        for(Field f : getOneToOnes()) {
            if (f.getName().equals(fieldName)) {
                return true;
            }
        }
        for(Set<String> uniqueConstraint : uniqueConstraints) {
            if (1 == uniqueConstraint.size() && uniqueConstraint.contains(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Field> getAllFields() {
        final Map<String, Field> returnValue = new TreeMap<String, Field>();
        for(Field f : getFields()) {
            returnValue.put(f.getName(), f);
        }
        for(Field f : getOneToOnes()) {
            returnValue.put(f.getName(), f);
        }
        for(Field f : getManyToOnes()) {
            returnValue.put(f.getName(), f);
        }
        for(Field f : getManyToManys()) {
            returnValue.put(f.getName(), f);
        }
        return returnValue;
    }

    public List<Set<Field>> getUniqueFieldsSets() {
        final List<Set<Field>> returnValue = new ArrayList<Set<Field>>();
        Map<String, Field> allFields = getAllFields();
        for(Set<String> uniqueConstraint : uniqueConstraints) {
            if (1 < uniqueConstraint.size()) {
                Set<Field> fieldsSet = new TreeSet<Field>();
                for(String fieldName : uniqueConstraint) {
                    for(Field column : allFields.values()) {
                        if (fieldName.equals(column.getColumnName())) {
                            fieldsSet.add(column);
                            break;
                        }
                    }
                    if (null != parent && parent.getName().equals(fieldName)) {
                        fieldsSet.add(parent);
                    }
                }
                returnValue.add(fieldsSet);
            }
        }
        return returnValue;
    }

    public Field getFirstUniqueField() {
        Field returnValue = null;

        for(Entry<String, Field> entry : getAllFields().entrySet()) {
            if (isUnique(entry.getKey())) {
                returnValue = entry.getValue();
                break;
            }
        }

        return returnValue;
    }

    public Set<Field> getManyToManys() {
        return manyToManys;
    }

    public Map<String, Field> getMappedBy() {
        return mappedBy;
    }

    @Override
    public String toString() {
        return getClassName() + "<" + getSimpleName() + ">";
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * If there is no <code>@@Table(name)</code> annotation, just return the simpleName
     * 
     * @return
     */
    public String getTableName() {
        if (null == tableName) {
            return getSimpleName();
        }
        return tableName;
    }

    /** Caching */
    public boolean shouldCache() {
        return null != cached;
    }

    public boolean shouldCachePages() {
        return null != cached && cached.cachePages();
    }

    public Set<Field> getOneToOnes() {
        return oneToOnes;
    }

    public Set<Entity> getDependsOn() {
        return dependsOn;
    }

    @Override
    public int compareTo(Entity other) {
        return this.getClassName().compareTo(other.getClassName());
    }

    public void setParent(Field parent) {
        this.parent = parent;
    }

    public Field getParent() {
        return parent;
    }

    public void setAncestors(List<Entity> ancestors) {
        this.ancestors = ancestors;
    }

    public List<Entity> getAncestors() {
        return ancestors;
    }

    public void setParents(List<Entity> parents) {
        this.parents = parents;
    }

    public List<Entity> getParents() {
        return parents;
    }

    public Set<Entity> getChildren() {
        return children;
    }

    public final Field getCreatedDate() {
        return createdDate;
    }

    public final void setCreatedDate(Field createdDate) {
        this.createdDate = createdDate;
    }

    public final Field getCreatedBy() {
        return createdBy;
    }

    public final void setCreatedBy(Field createdBy) {
        this.createdBy = createdBy;
    }

    public final Field getUpdatedDate() {
        return updatedDate;
    }

    public final void setUpdatedDate(Field updatedDate) {
        this.updatedDate = updatedDate;
    }

    public final Field getUpdatedBy() {
        return updatedBy;
    }

    public final void setUpdatedBy(Field updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Field getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Field geoLocation) {
        this.geoLocation = geoLocation;
    }

    public Cached getCached() {
        return cached;
    }

    public void setCached(Cached cached) {
        this.cached = cached;
    }
}
