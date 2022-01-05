package net.bnb1.kradle

import io.mockk.every
import io.mockk.mockk
import net.bnb1.kradle.features.FeatureRegistry
import net.bnb1.kradle.features.FeatureSetRegistry
import net.bnb1.kradle.features.PropertiesRegistry
import net.bnb1.kradle.presets.PresetRegistry
import org.gradle.api.Project
import org.gradle.api.Transformer
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.extra
import java.util.function.BiFunction

object Mocks {

    fun project() = mockk<Project>(relaxed = true) {
        every { extra.get("tracer") } returns Tracer()
        every { extra.get("featureRegistry") } returns FeatureRegistry()
        every { extra.get("propertiesRegistry") } returns PropertiesRegistry()
        every { extra.get("presetRegistry") } returns PresetRegistry()
        every { extra.get("featureSetRegistry") } returns FeatureSetRegistry()
        every { objects } returns mockk {
            every { property(String::class.java) } returns SimpleProperty()
            every { property(Boolean::class.java) } returns SimpleProperty()
            every { setProperty(Integer::class.java) } returns SimpleSetProperty()
            every { setProperty(String::class.java) } returns SimpleSetProperty()
        }
    }

    class SimpleProperty<T> : Property<T> {

        private var value: T? = null

        override fun isPresent() = value != null
        override fun get(): T = value!!
        override fun getOrNull(): T? = value
        override fun getOrElse(defaultValue: T): T = value ?: defaultValue

        override fun set(value: T?) {
            this.value = value
        }

        override fun convention(value: T?): Property<T> {
            this.value = value
            return this
        }

        override fun value(value: T?): Property<T> {
            this.value = value
            return this
        }

        override fun <S : Any?> map(transformer: Transformer<out S, in T>): Provider<S> {
            TODO("Not yet implemented")
        }

        override fun <S : Any?> flatMap(transformer: Transformer<out Provider<out S>, in T>): Provider<S> {
            TODO("Not yet implemented")
        }

        override fun orElse(value: T): Provider<T> {
            TODO("Not yet implemented")
        }

        override fun orElse(p0: Provider<out T>): Provider<T> {
            TODO("Not yet implemented")
        }

        override fun forUseAtConfigurationTime(): Provider<T> {
            TODO("Not yet implemented")
        }

        override fun <B : Any?, R : Any?> zip(p0: Provider<B>, p1: BiFunction<T, B, R>): Provider<R> {
            TODO("Not yet implemented")
        }

        override fun finalizeValue() {
            TODO("Not yet implemented")
        }

        override fun finalizeValueOnRead() {
            TODO("Not yet implemented")
        }

        override fun disallowChanges() {
            TODO("Not yet implemented")
        }

        override fun disallowUnsafeRead() {
            TODO("Not yet implemented")
        }

        override fun set(provider: Provider<out T>) {
            TODO("Not yet implemented")
        }

        override fun value(provider: Provider<out T>): Property<T> {
            TODO("Not yet implemented")
        }

        override fun convention(provider: Provider<out T>): Property<T> {
            TODO("Not yet implemented")
        }
    }

    class SimpleSetProperty<T> : SetProperty<T> {

        private var value: MutableSet<T>? = null

        override fun isPresent() = value != null
        override fun get(): MutableSet<T> = value!!
        override fun getOrNull(): MutableSet<T>? = value
        override fun getOrElse(defaultValue: MutableSet<T>): MutableSet<T> = value ?: defaultValue

        override fun set(elements: MutableIterable<T>?) {
            value = elements?.toMutableSet()
        }

        override fun value(elements: MutableIterable<T>?): SetProperty<T> {
            value = elements?.toMutableSet()
            return this
        }

        override fun empty(): SetProperty<T> {
            value = mutableSetOf()
            return this
        }

        override fun add(element: T) {
            if (value == null) {
                value = mutableSetOf()
            }
            value!!.add(element)
        }

        override fun addAll(elements: MutableIterable<T>) {
            if (value == null) {
                value = mutableSetOf()
            }
            value!!.addAll(elements)
        }

        override fun addAll(vararg elements: T) {
            if (value == null) {
                value = mutableSetOf()
            }
            value!!.addAll(elements)
        }

        override fun convention(elements: MutableIterable<T>?): SetProperty<T> {
            value = elements?.toMutableSet()
            return this
        }

        override fun <S : Any?> map(transformer: Transformer<out S, in MutableSet<T>>): Provider<S> {
            TODO("Not yet implemented")
        }

        override fun <S : Any?> flatMap(transformer: Transformer<out Provider<out S>, in MutableSet<T>>): Provider<S> {
            TODO("Not yet implemented")
        }

        override fun orElse(value: MutableSet<T>): Provider<MutableSet<T>> {
            TODO("Not yet implemented")
        }

        override fun orElse(p0: Provider<out MutableSet<T>>): Provider<MutableSet<T>> {
            TODO("Not yet implemented")
        }

        override fun forUseAtConfigurationTime(): Provider<MutableSet<T>> {
            TODO("Not yet implemented")
        }

        override fun <B : Any?, R : Any?> zip(p0: Provider<B>, p1: BiFunction<MutableSet<T>, B, R>): Provider<R> {
            TODO("Not yet implemented")
        }

        override fun finalizeValue() {
            TODO("Not yet implemented")
        }

        override fun finalizeValueOnRead() {
            TODO("Not yet implemented")
        }

        override fun disallowChanges() {
            TODO("Not yet implemented")
        }

        override fun disallowUnsafeRead() {
            TODO("Not yet implemented")
        }

        override fun set(provider: Provider<out MutableIterable<T>>) {
            TODO("Not yet implemented")
        }

        override fun value(provider: Provider<out MutableIterable<T>>): SetProperty<T> {
            TODO("Not yet implemented")
        }

        override fun add(provider: Provider<out T>) {
            TODO("Not yet implemented")
        }

        override fun addAll(provider: Provider<out MutableIterable<T>>) {
            TODO("Not yet implemented")
        }

        override fun convention(provider: Provider<out MutableIterable<T>>): SetProperty<T> {
            TODO("Not yet implemented")
        }
    }
}
