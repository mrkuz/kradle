plugins {
    `java`
}

tasks.named<Jar>("jar").configure {
    manifest {
        attributes(Pair("Premain-Class", "net.bnb1.stop.Agent"))
    }
}
