package felix.duan.superherodb

import felix.duan.superherodb.model.SuperHeroData

fun SuperHeroData.PowerStats.format(): String {
    return "INT: $intelligence STR: $strength SPD: $speed\nDUR: $durability POW: $power COM: $combat"
}