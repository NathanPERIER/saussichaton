package fr.nperier.saussichaton.injection;

import fr.nperier.saussichaton.injection.annotations.AutoResolve;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResolverTests {

    private Resolver resolver;
    private Service service;
    private A a;
    private B b;

    @BeforeEach
    public void init() {
        resolver = new Resolver();
        service = new Service();
        a = new A();
        b = new B();
        resolver.addService(service);
        resolver.setNamedObject("a", a);
        resolver.setNamedObject("b", b);
        resolver.setNamedObject("hello", "bonjour");
    }

    @Test
    public void testEmpty() {
        Resolvable_Empty _resolved = resolver.resolve(Resolvable_Empty.class);
    }

    @Test
    public void testService() {
        Resolvable_Service resolved = resolver.resolve(Resolvable_Service.class);
        assertSame(service, resolved.service);
    }

    @Test
    public void testNamedObjectSameType() {
        Resolvable_AA resolved = resolver.resolve(Resolvable_AA.class);
        assertSame(a, resolved.a);
    }

    @Test
    public void testNamedObjectChildType() {
        Resolvable_AB resolved = resolver.resolve(Resolvable_AB.class);
        assertSame(b, resolved.b);
    }

    @Test
    public void testNamedObjectParentType() {
        assertThrows(InjectionException.class, () -> resolver.resolve(Resolvable_BA.class));
    }

    @Test
    public void testNamedObjectBadType() {
        assertThrows(InjectionException.class, () -> resolver.resolve(Resolvable_StringA.class));
    }

    @Test
    public void testSeveralConstructors() {
        ResolvableSeveral resolved = resolver.resolve(ResolvableSeveral.class);
        assertSame(a, resolved.a);
        assertEquals("bonjour", resolved.hello);
    }

    @Test
    public void testNoAvailableConstructor() {
        assertThrows(InjectionException.class, () -> resolver.resolve(ResolvableNone.class));
    }

    @Test
    public void testTooFewAnnotations() {
        assertThrows(InjectionException.class, () -> resolver.resolve(ResolvableTooFew.class));
    }

    @Test
    public void testTooManyAnnotations() {
        assertThrows(InjectionException.class, () -> resolver.resolve(ResolvableTooMany.class));
    }

    @Test
    public void testBadParam() {
        assertThrows(InjectionException.class, () -> resolver.resolve(ResolvableBadParam.class));
    }

    @Test
    public void testReplaceService() {
        assertThrows(IllegalArgumentException.class, () -> resolver.addService(new Service()));
    }

    @Test
    public void testReplaceNamedObject() {
        A aa = new A();
        resolver.setNamedObject("a", aa);
        Resolvable_AA resolved = resolver.resolve(Resolvable_AA.class);
        assertSame(aa, resolved.a);
        assertNotSame(a, resolved.a);
    }




    private static class A { }

    private static class B extends A { }

    private static class Service { }


    private static class Resolvable_Empty implements Resolvable {
        public Resolvable_Empty() { }
    }

    private static class Resolvable_Service implements Resolvable {
        public final Service service;
        public Resolvable_Service(final Service service) { this.service = service; }
    }

    private static class Resolvable_AA implements Resolvable {
        public final A a;
        public Resolvable_AA(final A a) { this.a = a; }
    }

    private static class Resolvable_AB implements Resolvable {
        public final A b;
        public Resolvable_AB(final A b) { this.b = b; }
    }

    private static class Resolvable_BA implements Resolvable {
        public final B a;
        public Resolvable_BA(final B a) { this.a = a; }
    }

    private static class Resolvable_StringA implements Resolvable {
        public final String a;
        public Resolvable_StringA(final String a) { this.a = a; }
    }

    private static class ResolvableSeveral implements Resolvable {
        public final A a;
        public final String hello;
        public ResolvableSeveral() { this.a = null; this.hello = ""; }
        @AutoResolve
        public ResolvableSeveral(final A a, final String hello) { this.a = a; this.hello = hello; }
    }

    private static class ResolvableNone implements Resolvable {
        private ResolvableNone() { }
    }

    private static class ResolvableTooFew implements Resolvable {
        public final String s;
        public ResolvableTooFew() { this.s = ""; }
        public ResolvableTooFew(final String hello) { this.s = hello; }
    }

    private static class ResolvableTooMany implements Resolvable {
        public final String s;
        @AutoResolve
        public ResolvableTooMany() { this.s = ""; }
        @AutoResolve
        public ResolvableTooMany(final String hello) { this.s = hello; }
    }

    private static class ResolvableBadParam implements Resolvable {
        public final String s;
        @AutoResolve
        public ResolvableBadParam(final String s) { this.s = s; }
    }

}
