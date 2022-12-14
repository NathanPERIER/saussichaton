package fr.nperier.saussichaton.injection;

import fr.nperier.saussichaton.injection.annotations.AutoResolve;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Object that can initialise an object of a given class through dependency injection.
 */
public class Resolver {

    private final Map<Class<?>,Object> services;
    private final Map<String,Object> namedObjects;

    public Resolver() {
        this.services = new HashMap<>();
        this.namedObjects = new HashMap<>();
    }

    /**
     * Adds an object as a service, meaning that the parameters will be resolved based on the class.
     * @throws IllegalArgumentException if a service is already registered for this class
     */
    public void addService(final Object service) throws IllegalArgumentException {
        Class<?> clazz = service.getClass();
        if(services.containsKey(service.getClass())) {
            throw new IllegalArgumentException("A service is already registered for class " + clazz.getName());
        }
        services.put(service.getClass(), service);
    }

    /**
     * Sets the value of a named object. The parameters will be resolved based on the name.
     */
    public void setNamedObject(String name, Object obj) {
        namedObjects.put(name, obj);
    }

    /**
     * Method that finds a constructor suitable for dependency injection.
     * If the class has only one constructor, it will always be picked.
     * Else, the class is supposed to have exactly one constructor annotated with {@link AutoResolve}.
     * @throws InjectionException if no suitable constructor is found.
     */
    private static <T extends Resolvable> Constructor<?> getResolvableConstructor(Class<T> clazz) throws InjectionException {
        if(clazz.getConstructors().length == 1) {
            return clazz.getConstructors()[0];
        }
        List<Constructor<?>> candidates = Arrays.stream(clazz.getConstructors())
                .filter(cons -> cons.getAnnotation(AutoResolve.class) != null)
                .collect(Collectors.toList());
        if(candidates.size() > 1) {
            throw new InjectionException("Class " + clazz.getName() + " has too many constructors annotated for injection");
        }
        if(candidates.size() == 0) {
            throw new InjectionException("Class " + clazz.getName() + " has no constructor annotated for injection");
        }
        return candidates.get(0);
    }

    /**
     * Initialises an object of a given class using the services and name objects that are registered.
     * @throws InjectionException if the class cannot be injected.
     */
    @SuppressWarnings("unchecked")
    public <T extends Resolvable> T resolve(Class<T> clazz) throws InjectionException {
        Constructor<?> cons = getResolvableConstructor(clazz);
        Object[] args = Arrays.stream(cons.getParameters())
                .map(this::resolveParameter)
                .toArray();
        Object res;
        try {
            res = cons.newInstance(args);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            throw new InjectionException("An error occurred during the resolving of class " + clazz.getName(), e);
        }
        if(!clazz.isAssignableFrom(res.getClass())) {
            // This case is almost impossible to trigger, it would require to modify the class after compilation
            // to make the constructor that returns an object that is not an instance of this class.
            throw new InjectionException("Got object of unexpected type " + res.getClass().getName()
                    + " while resolving " + clazz.getName());
        }
        // Here we have a warning for unchecked cast, but according to the code above
        // the cast should always be possible, so we just ignore it
        return (T) res;
    }

    /**
     * Attempts to resolve the value for a parameter.
     * If there is a service registered for the class of the parameter, yields the service.
     * Else, searches for a named object with the same name as that of the parameter.
     * @throws InjectionException If no value was found for the parameter.
     */
    private Object resolveParameter(Parameter param) throws InjectionException {
        if(services.containsKey(param.getType())) {
            return services.get(param.getType());
        }
        if(namedObjects.containsKey(param.getName())) {
            return namedObjects.get(param.getName());
        }
        throw new InjectionException("Impossible to resolve parameter " + param.getName() + " of type " + param.getType());
    }

    @SuppressWarnings("unchecked")
    public <T> T getNamedObject(final String name) {
        return (T) namedObjects.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(final Class<T> clazz) {
        return (T) services.get(clazz);
    }

}
