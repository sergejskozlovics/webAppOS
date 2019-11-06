package org.webappos.status;

import org.webappos.registry.IRegistry;

/**
 * An interface for specifying status values. Status info is referenced by a string key (usually, a fully qualified name of some property).
 * The value of each key can be updated several times at runtime to reflect the updated status.
 * @author Sergejs Kozlovics
 *
 */
public interface IStatus extends IRegistry {
}
