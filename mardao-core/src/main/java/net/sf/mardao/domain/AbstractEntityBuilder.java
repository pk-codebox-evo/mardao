package net.sf.mardao.domain;

/*
 * #%L
 * mardao-core
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

/**
 * Builder for Dao Entities.
 *
 * @author osandstrom Date: 2014-10-05 Time: 08:27
 */
public abstract class AbstractEntityBuilder<T> {

  protected final T entity;

  protected AbstractEntityBuilder(T entity) {
    this.entity = entity;
  }

  public T build() {
    return entity;
  }
}
