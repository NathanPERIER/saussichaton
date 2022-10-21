package fr.nperier.saussichaton.injection;

import fr.nperier.saussichaton.errors.InjectionException;
import fr.nperier.saussichaton.injection.annotations.AutoResolve;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Resolver {

    private final Map<Class<?>,Object> services;
    private final Map<String,Object> namedObjects;

    public Resolver() {
        this.services = new HashMap<>();
        this.namedObjects = new HashMap<>();
    }

    public void addService(final Object service) throws IllegalArgumentException {
        Class<?> clazz = service.getClass();
        if(services.containsKey(service.getClass())) {
            throw new IllegalArgumentException("A service is already registered for class " + clazz.getName());
        }
        services.put(service.getClass(), service);
    }

    public void setNamedObject(String name, Object obj) {
        namedObjects.put(name, obj);
    }

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
            throw new InjectionException("Got object of unexpected type " + res.getClass().getName()
                    + " while resolving " + clazz.getName());
        }
        return (T) res;
    }

    private Object resolveParameter(Parameter param) throws InjectionException {
        if(services.containsKey(param.getType())) {
            return services.get(param.getType());
        }
        if(namedObjects.containsKey(param.getName())) {
            return namedObjects.get(param.getName());
        }
        throw new InjectionException("Impossible to resolve parameter " + param.getName() + " of type " + param.getType());
    }

}
