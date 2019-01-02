/*
 * Copyright 2018 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.consensys.cava.devp2p

import net.consensys.cava.bytes.Bytes
import net.consensys.cava.bytes.Bytes32
import net.consensys.cava.crypto.SECP256K1

internal enum class PacketType(
  val typeId: Byte
) {

  PING(0x01) {
    override fun decode(
      payload: Bytes,
      hash: Bytes32,
      publicKey: SECP256K1.PublicKey,
      signature: SECP256K1.Signature
    ) = PingPacket.decode(payload, hash, publicKey, signature)
  },
  PONG(0x02) {
    override fun decode(
      payload: Bytes,
      hash: Bytes32,
      publicKey: SECP256K1.PublicKey,
      signature: SECP256K1.Signature
    ) = PongPacket.decode(payload, hash, publicKey, signature)
  },
  FIND_NODE(0x03) {
    override fun decode(
      payload: Bytes,
      hash: Bytes32,
      publicKey: SECP256K1.PublicKey,
      signature: SECP256K1.Signature
    ) = FindNodePacket.decode(payload, hash, publicKey, signature)
  },
  NEIGHBORS(0x04) {
    override fun decode(
      payload: Bytes,
      hash: Bytes32,
      publicKey: SECP256K1.PublicKey,
      signature: SECP256K1.Signature
    ) = NeighborsPacket.decode(payload, hash, publicKey, signature)
  };

  companion object {
    private const val MAX_VALUE: Byte = 0x7f
    private val INDEX = arrayOfNulls<PacketType?>(MAX_VALUE.toInt())

    init {
      // populate an array by packet type id for index-based lookup in `forType(Byte)`
      PacketType.values().forEach { type -> INDEX[type.typeId.toInt()] = type }
    }

    fun forType(typeId: Byte): PacketType? {
      return INDEX[typeId.toInt()]
    }
  }

  init {
    require(typeId <= PacketType.MAX_VALUE) { "Packet typeId must be in range [0x00, 0x80)" }
  }

  abstract fun decode(
    payload: Bytes,
    hash: Bytes32,
    publicKey: SECP256K1.PublicKey,
    signature: SECP256K1.Signature
  ): Packet
}
